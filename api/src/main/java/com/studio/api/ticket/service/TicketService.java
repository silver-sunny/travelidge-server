package com.studio.api.ticket.service;

import static com.studio.api.ticket.service.serializer.TicketSerializer.toTicketEntity;
import static com.studio.core.global.exception.ErrorCode.NOT_VALID_STATE;
import static com.studio.core.global.exception.ErrorCode.ORDER_NOT_FOUND;
import static com.studio.core.global.exception.ErrorCode.TICKET_NOT_AVAILABLE;
import static com.studio.core.global.exception.ErrorCode.TICKET_NOT_FOUND;

import com.studio.core.global.enums.Channels;
import com.studio.core.global.enums.order.CancelOrReturnState;
import com.studio.core.global.enums.order.ProductOrderState;
import com.studio.core.global.enums.order.TicketState;
import com.studio.core.global.enums.order.TicketUsedState;
import com.studio.core.global.exception.CustomException;
import com.studio.core.order.entity.OrderEntity;
import com.studio.core.order.repository.OrderJpaRepository;
import com.studio.core.ticket.entity.TicketEntity;
import com.studio.core.ticket.repository.TicketJpaRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketJpaRepository ticketRepository;

    private final OrderJpaRepository orderRepository;


    /**
     * 티켓 사용처리
     *
     * @param ticketKey 티켓키값
     */
    public void useTicket(String ticketKey) {
        TicketEntity ticket = ticketRepository.findByTicketKey(ticketKey)
            .orElseThrow(() -> new CustomException(TICKET_NOT_FOUND));

        if (!_checkUseTicket(ticket)) {
            throw new CustomException(TICKET_NOT_AVAILABLE);

        }
        ticket.useTicket(ticketKey, TicketUsedState.USED);
        ticketRepository.save(ticket);
    }

    /**
     * 티켓 발급
     *
     * @param orderId 발급할 주문번호
     */
    public void insertTicket(Long orderId) {

        // 주문내역있는지 조회
        OrderEntity order = orderRepository.findById(orderId)
            .orElseThrow(() -> new CustomException(ORDER_NOT_FOUND));
        // 고유한 티켓키 생성, 저장

        if (!_checkInsertTicketState(order)) {
            throw new CustomException(NOT_VALID_STATE);
        }

        String ticketKey = _generateUniqueTicketKey();  // 고유한 ticketKey 생성
        TicketEntity ticket = toTicketEntity(ticketKey, TicketUsedState.AVAILABLE, null, order,
            order.getMember());

        ticketRepository.save(ticket);
        orderRepository.updateProductOrderOfTicket(order.getId(),
            TicketState.AVAILABLE, ProductOrderState.DELIVERED);
    }

    private String _generateUniqueTicketKey() {
        String ticketKey;

        do {
            ticketKey = UUID.randomUUID().toString();
        } while (ticketRepository.findByTicketKey(ticketKey)
            .isPresent());  // Check if the ticketKey is already taken

        return ticketKey;
    }


    private boolean _checkInsertTicketState(OrderEntity order) {
        return (ProductOrderState.PAYED.equals(order.getOrderState())
            || ProductOrderState.PURCHASE_DECIDED.equals(order.getOrderState())) &&
            TicketState.NON_ISSUED.equals(order.getTicketState()) &&
            Channels.TRAVELIDGE.equals(order.getChannel());
    }

    private boolean _checkUseTicket(TicketEntity ticket) {
        CancelOrReturnState cancelOrReturnState = ticket.getOrder().getCancelOrReturnState();
        return TicketUsedState.AVAILABLE.equals(ticket.getIsUsed()) &&
            (CancelOrReturnState.NOTHING.equals(cancelOrReturnState)
                || CancelOrReturnState.CANCEL_REJECT.equals(cancelOrReturnState) ||
                CancelOrReturnState.RETURN_REJECT.equals(cancelOrReturnState) ||
                CancelOrReturnState.PURCHASE_DECISION_REQUEST.equals(cancelOrReturnState) ||
                CancelOrReturnState.PURCHASE_DECISION_HOLDBACK.equals(cancelOrReturnState) ||
                CancelOrReturnState.PURCHASE_DECISION_HOLDBACK_RELEASE.equals(cancelOrReturnState));
    }
}
