package com.studio.batch.ticket.processor;

import com.studio.core.order.entity.OrderEntity;
import com.studio.core.order.repository.OrderJpaRepository;
import com.studio.core.ticket.entity.TicketEntity;
import com.studio.core.ticket.repository.TicketJpaRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TicketWriter implements ItemWriter<TicketEntity> {

    private final TicketJpaRepository ticketRepository;
    private final OrderJpaRepository orderRepository;


    @Transactional
    @Override
    public void write(Chunk<? extends TicketEntity> chunk) throws Exception {
        try {
            List<? extends TicketEntity> ticketEntities = chunk.getItems();

            // 전체 수정
            ticketRepository.saveAll(ticketEntities);
            // TicketEntity에 포함된 ProductOrder 저장 (TicketEntity와 연관된 ProductOrder를 가져오는 방식)
            List<OrderEntity> productOrders = ticketEntities.stream()
                    .map(TicketEntity::getOrder)  // getProductOrder()는 TicketEntity에서 ProductOrder를 추출하는 메서드
                    .collect(Collectors.toList());

            orderRepository.saveAll(productOrders);
        } catch (Exception e) {
            log.error("Error while saving ticket: {}", e.getMessage(), e);
            throw new RuntimeException("Error while saving ticket", e);
        }
    }
}



