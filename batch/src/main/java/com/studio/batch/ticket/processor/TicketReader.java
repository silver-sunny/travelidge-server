package com.studio.batch.ticket.processor;


import static com.studio.batch.ticket.service.serializer.TicketSerializer.toTicketEntity;

import com.studio.core.global.enums.Channels;
import com.studio.core.global.enums.order.CancelOrReturnState;
import com.studio.core.global.enums.order.ProductOrderState;
import com.studio.core.global.enums.order.TicketState;
import com.studio.core.global.enums.order.TicketUsedState;
import com.studio.core.global.naver.dto.NaverDataDto;
import com.studio.core.global.naver.dto.NaverProductOrderIdsDto;
import com.studio.core.global.naver.dto.NaverProductOrderInfo;
import com.studio.core.global.naver.service.NaverOrderApiService;
import com.studio.core.order.dto.naver.NaverDisPatchProductOrderInfo;
import com.studio.core.order.entity.OrderEntity;
import com.studio.core.order.repository.OrderJpaRepository;
import com.studio.core.ticket.entity.TicketEntity;
import com.studio.core.ticket.repository.TicketJpaRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TicketReader implements ItemReader<TicketEntity> {


    private final TicketJpaRepository ticketRepository;

    private final OrderJpaRepository orderRepository;

    private final NaverOrderApiService naverOrderService;
    private List<TicketEntity> naverDataList;

    private int nextDataIndex = 0;

    @Override
    public TicketEntity read() throws Exception {
        if (naverDataList == null) {
            naverDataList = fetchNaverProductOrders();
            nextDataIndex = 0;
        }

        TicketEntity nextData = null;
        if (nextDataIndex < naverDataList.size()) {
            nextData = naverDataList.get(nextDataIndex);
            nextDataIndex++;
        }

        return nextData;
    }

    private List<TicketEntity> fetchNaverProductOrders() {
        List<OrderEntity> nonIssuedTickets = orderRepository.findNonIssuedTickets(
            TicketState.NON_ISSUED,
            Channels.NAVER,
            ProductOrderState.productOrderTicketApplyAvailableList(),
            CancelOrReturnState.cancelOrReturnStateTicketIssuedAvailableList()
        );

        List<TicketEntity> ticketEntities = new ArrayList<>();
        for (OrderEntity p : nonIssuedTickets) {
            try {
                NaverProductOrderInfo naverProductOrderInfo =
                    naverOrderService.getProductOrders(
                        new NaverProductOrderIdsDto(
                            Collections.singletonList(p.getChannelProductOrderId()))
                    );

                List<NaverDataDto> validData = naverProductOrderInfo.getData().stream()
                    .filter(naverDataDto -> naverDataDto.getProductOrder().getProductOrderStatus()
                        .isAvailable())
                    .filter(
                        naverDataDto -> naverDataDto.getCancel() == null || naverDataDto.getCancel()
                            .getClaimStatus().isUsable())
                    .filter(naverDataDto -> naverDataDto.getReturnDto() == null
                        || naverDataDto.getReturnDto().getClaimStatus().isUsable())
                    .collect(Collectors.toList());

                for (NaverDataDto dto : validData) {
                    NaverDisPatchProductOrderInfo dispatchOrderRes = naverOrderService.dispatchNaverProductOrder(
                        dto.getProductOrder().getProductOrderId());

                    if (dispatchOrderRes.getData().getSuccessProductOrderIds() != null
                        && !dispatchOrderRes.getData().getSuccessProductOrderIds().isEmpty()) {

                        NaverProductOrderInfo sucNaverProductOrderInfo = naverOrderService.getProductOrders(
                            new NaverProductOrderIdsDto(
                                dispatchOrderRes.getData().getSuccessProductOrderIds()));
                        Optional<NaverDataDto> sucNaverDate = sucNaverProductOrderInfo.getData()
                            .stream().filter(data -> data.getProductOrder().getProductOrderId()
                                .equals(p.getChannelProductOrderId())).findFirst();
                        if (sucNaverDate.isPresent()) {
                            p.updateTicketState(TicketState.AVAILABLE,
                                sucNaverDate.get().getProductOrder().getProductOrderStatus());

                            TicketEntity ticket = toTicketEntity(_generateUniqueTicketKey(),
                                TicketUsedState.AVAILABLE, null, p);
                            ticketEntities.add(ticket);
                        }

                    } else if (dispatchOrderRes.getData().getFailProductOrderInfos() != null
                        && !dispatchOrderRes.getData().getFailProductOrderInfos().isEmpty()) {
                        throw new IllegalArgumentException(
                            dispatchOrderRes.getData().getFailProductOrderInfos().toString());
                    }

                }


            } catch (Exception e) {
                log.error("Error processing ProductOrder with ID {}: {}", p.getId(), e.getMessage(),
                    e);
            }
        }
        return ticketEntities;
    }

    private String _generateUniqueTicketKey() {
        String ticketKey;

        do {
            ticketKey = UUID.randomUUID().toString();
        } while (ticketRepository.findByTicketKey(ticketKey)
            .isPresent());  // Check if the ticketKey is already taken

        return ticketKey;
    }

}


