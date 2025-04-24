package com.studio.api.order.service;


import static com.studio.core.global.exception.ErrorCode.NOT_EXPECT_CHANNEL;
import static com.studio.core.global.exception.ErrorCode.NOT_VALID_MEMBER;
import static com.studio.core.global.exception.ErrorCode.NOT_VALID_STATE;
import static com.studio.core.global.exception.ErrorCode.ORDER_NOT_FOUND;
import static com.studio.core.global.exception.ErrorCode.TICKET_USED;

import com.studio.api.product.service.PaymentService;
import com.studio.api.product.service.ProductCommonService;
import com.studio.core.global.enums.Channels;
import com.studio.core.global.enums.order.CancelOrReturnState;
import com.studio.core.global.enums.order.ProductOrderState;
import com.studio.core.global.enums.order.TicketUsedState;
import com.studio.core.global.enums.product.naver.CancelReason;
import com.studio.core.global.enums.product.naver.ReturnReason;
import com.studio.core.global.exception.CustomException;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.order.dto.GetReturnCountResponse;
import com.studio.core.order.entity.OrderEntity;
import com.studio.core.order.repository.OrderJpaRepository;
import com.studio.core.ticket.entity.TicketEntity;
import com.studio.core.ticket.repository.TicketJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderJpaRepository orderRepository;

    private final ProductCommonService productCommonService;

    private final TicketJpaRepository ticketRepository;
    private final PaymentService paymentService;


    /**
     * 반품
     *
     * @param orderId
     * @param returnReason
     */
    public void returnOrder(Long orderId, ReturnReason returnReason) {
        OrderEntity order = _getProductOrder(orderId);
        //반품

        if (ProductOrderState.DELIVERED.equals(order.getOrderState())) {
            // 티켓이 사용하지 않은 상태여야됨

            ticketRepository.findByOrder_id(orderId).ifPresent(ticket -> {
                if (TicketUsedState.USED.equals(ticket.getIsUsed())) {
                    throw new CustomException(TICKET_USED);
                }
            });

            order.returnOrder(returnReason);
            orderRepository.save(order);
            productCommonService.increaseProductStock(order.getPurchaseQuantity(),
                order.getProduct().getId());
        } else {
            throw new CustomException(NOT_VALID_STATE,
                " > 반품 가능한 상태가 아닙니다.: " + order.getOrderState());
        }
    }

    /**
     * 반품 승인 (티켓이 발급된 후 승인)
     */
    public void returnApprove(Long orderId, MemberAuthEntity member) {
        OrderEntity order = _getProductOrder(orderId);

        // 반품 요청 있는지 확인
        if (CancelOrReturnState.RETURN_REQUEST.equals(order.getCancelOrReturnState())) {

            // 티켓이 사용하지 않은 상태여야됨
            ticketRepository.findByOrder_id(orderId).ifPresent(ticket -> {
                if (TicketUsedState.USED.equals(ticket.getIsUsed())) {
                    throw new CustomException(TICKET_USED);
                }
                ticket.updateTicketState(TicketUsedState.NOT_CONFIRM);
                ticketRepository.save(ticket);
            });

            //반품
            order.returnApproveOrder();

            orderRepository.save(order);
            productCommonService.increaseProductStock(order.getPurchaseQuantity(),
                order.getProduct().getId());

            paymentService.cancelPayment(order.getChannelOrderId(), order.getCancelOrReturnReason());


        } else {
            throw new CustomException(NOT_VALID_STATE,
                " > 반품 요청상태가 없습니다.: " + order.getCancelOrReturnState());
        }

    }


    /**
     * 반품 철회(거부)
     *
     * @param orderId
     * @param rejectReturnReason
     */
    public void returnRejectOrder(Long orderId, String rejectReturnReason) {

        OrderEntity order = _getProductOrder(orderId);
        if (CancelOrReturnState.RETURN_REQUEST.equals(order.getCancelOrReturnState())) {

            order.returnRejectOrder(rejectReturnReason);
            orderRepository.save(order);

        } else {
            throw new CustomException(NOT_VALID_STATE,
                " > 반품 요청상태가 없습니다.: " + order.getCancelOrReturnState());
        }

        //
//        // 취소/ 반품 상태가 없거나, 반품 철회, 취소 철회 인경우만 가능
//        if (!(this.cancelOrReturnState.equals(CancelOrReturnState.ETC) ||
//                this.cancelOrReturnState.equals(CancelOrReturnState.CANCEL_REJECT) ||
//                this.cancelOrReturnState.equals(CancelOrReturnState.RETURN_REJECT) ||
//                this.cancelOrReturnState.equals(CancelOrReturnState.PURCHASE_DECISION_REQUEST) ||
//                this.cancelOrReturnState.equals(CancelOrReturnState.PURCHASE_DECISION_HOLDBACK_RELEASE) ||
//                this.cancelOrReturnState.equals(CancelOrReturnState.PURCHASE_DECISION_HOLDBACK) ||
//                this.cancelOrReturnState.equals(CancelOrReturnState.ADMIN_CANCEL_REJECT))
//        ) {
//            throw new ConditionFailException("해당 주문은 취소/ 반품할수 없습니다.");
//
//        }
//
//        // 주문 상태가 결제완료, 배송완료, 구매확정, 결제대기, 배송중 일경우만 가능
//        if (!(this.orderState.equals(ProductOrderState.PAYED) ||
//                this.orderState.equals(ProductOrderState.DELIVERED) ||
//                this.orderState.equals(ProductOrderState.PURCHASE_DECIDED) ||
//                this.orderState.equals(ProductOrderState.PAYMENT_WAITING) ||
//                this.orderState.equals(ProductOrderState.DELIVERING))
//        ) {
//            throw new ConditionFailException("해당 주문은 취소/ 반품할수 없습니다.");
//
//        }
//        // 티켓상태가 발급전,발급 완료인 겅우에만 가능
//        if (!(this.ticketState.equals(TicketState.NON_ISSUED) || this.ticketState.equals(TicketState.AVAILABLE))) {
//            throw new ConditionFailException("티켓을 사용했습니다.");
//        }

//        if (Chann) {
//            productOrderService.updateProductOrderCancelOrReturnState(orderNo, rejectReturnReason, CancelOrReturnStateEnum.RETURN_REJECT);
//        } else if (entity.getChannelNo() == ChannelEnum.NAVER.getIndex()) {
//            naverProductStateDao.naverReturnReject(orderNo, rejectReturnReason);
//        }

    }

    // 취소
    public void cancelOrder(Long orderId, CancelReason cancelReason) {
        OrderEntity order = _getProductOrder(orderId);

        if (ProductOrderState.PAYED.equals(order.getOrderState())) {

            order.cancelOrder(cancelReason, "판매자");
            orderRepository.save(order);
            productCommonService.increaseProductStock(order.getPurchaseQuantity(),
                order.getProduct().getId());

            paymentService.cancelPayment(order.getChannelOrderId(), cancelReason.getMeaning());
        } else {
            throw new CustomException(NOT_VALID_STATE,
                " > 취소 가능한 상태가 아닙니다.: " + order.getOrderState() + "->"
                    + order.getCancelOrReturnState());
        }
    }


    public void cancelRequestOrder(Long orderId, CancelReason cancelReason,
        MemberAuthEntity member) {
        OrderEntity order = _getProductOrder(orderId);

        if (ProductOrderState.PAYED.equals(order.getOrderState())) {

            if (!order.getMember().getMemberNo().equals(member.getMemberNo())) {
                throw new CustomException(NOT_VALID_MEMBER);
            }

            order.cancelOrder(cancelReason, "구매자");
            orderRepository.save(order);


        } else {
            throw new CustomException(NOT_VALID_STATE,
                " > 취소요청 가능한 상태가 아닙니다.: " + order.getOrderState() + "->"
                    + order.getCancelOrReturnState());
        }
    }

    /**
     * 반품 요청
     *
     * @param orderId
     * @param returnReason
     */
    public void returnRequestOrder(Long orderId, ReturnReason returnReason,
        MemberAuthEntity member) {
        OrderEntity order = _getProductOrder(orderId);
        //반품

        if (ProductOrderState.DELIVERED.equals(order.getOrderState())) {

            if (!order.getMember().getMemberNo().equals(member.getMemberNo())) {

                throw new CustomException(NOT_VALID_MEMBER);
            }

            // 티켓이 사용하지 않은 상태여야됨
            Optional<TicketEntity> ticket = ticketRepository.findByOrder_id(orderId);
            if (ticket.isPresent()) {
                TicketEntity existTicket = ticket.get();

                if (TicketUsedState.USED.equals(existTicket.getIsUsed())) {
                    throw new CustomException(TICKET_USED);
                }
                existTicket.updateTicketState(TicketUsedState.NOT_CONFIRM);
                ticketRepository.save(existTicket);
            }
            order.returnOrderRequest(returnReason);
            orderRepository.save(order);

        } else {
            throw new CustomException(NOT_VALID_STATE,
                " > 반품 가능한 상태가 아닙니다.: " + order.getOrderState());
        }
    }

    /**
     * 반품요청한것을 취소
     *
     * @param orderId
     */
    public void returnCancelOrder(Long orderId, MemberAuthEntity member) {
        OrderEntity order = _getProductOrder(orderId);
        if (CancelOrReturnState.RETURN_REQUEST.equals(order.getCancelOrReturnState())) {

            if (!order.getMember().getMemberNo().equals(member.getMemberNo())) {

                throw new CustomException(NOT_VALID_MEMBER);
            }

            order.returnOrderCancel();
            orderRepository.save(order);


            Optional<TicketEntity> ticket = ticketRepository.findByOrder_id(orderId);

            if (ticket.isPresent()) {
                TicketEntity existTicket = ticket.get();
                existTicket.updateTicketState(TicketUsedState.AVAILABLE);
                ticketRepository.save(existTicket);
            }
        } else {
            throw new CustomException(NOT_VALID_STATE,
                " > 반품취소가 가능한 상태가 아닙니다.: " + order.getCancelOrReturnState());
        }
    }

    /**
     * 반품 갯수
     *
     * @return
     */
    public GetReturnCountResponse getReturnCount() {

        List<CancelOrReturnState> requestStates = List.of(
            CancelOrReturnState.CANCEL_REQUEST,
            CancelOrReturnState.RETURN_REQUEST
        );
        int requestCount = orderRepository.countProductOrderByCancelOrReturnStates(requestStates);

        List<CancelOrReturnState> completeStates = List.of(
            CancelOrReturnState.CANCEL_DONE,
            CancelOrReturnState.RETURN_DONE,
            CancelOrReturnState.ADMIN_CANCEL_DONE

        );
        int completeCount = orderRepository.countProductOrderByCancelOrReturnStates(completeStates);

        return new GetReturnCountResponse(requestCount, completeCount);
    }

    public Long countPurchaseByMember(MemberAuthEntity member) {
        return orderRepository.countByMemberAndOrderState(member);

    }

    private OrderEntity _getProductOrder(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(
            ORDER_NOT_FOUND));

        if (!order.getChannel().equals(Channels.TRAVELIDGE)) {
            // channelProductOrderId 없을경우 , 네이버가 아닐경우 에러
            throw new CustomException(NOT_EXPECT_CHANNEL);
        }

        return order;
    }


}
