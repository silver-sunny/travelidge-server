package com.studio.api.product.service;


import static com.studio.api.order.service.serializer.OrderSerializer.toOrderEntity;
import static com.studio.api.order.service.serializer.PaymentSerializer.toPaymentEntity;
import static com.studio.core.global.exception.ErrorCode.NOT_VALID_MEMBER;
import static com.studio.core.global.exception.ErrorCode.PAYMENT_AMOUNT_NOT_MATCH;
import static com.studio.core.global.exception.ErrorCode.PAYMENT_CONFIRM_STATUS_NOT_VALID;
import static com.studio.core.global.exception.ErrorCode.PAYMENT_REQUEST_NOT_FOUND;
import static com.studio.core.global.exception.ErrorCode.PRODUCT_NOT_FOUND;
import static com.studio.core.global.exception.ErrorCode.PRODUCT_NOT_VALID_ORDER_STATE;
import static com.studio.core.global.exception.ErrorCode.STOCK_OVER;

import com.studio.core.global.toss.TossPaymentApiService;
import com.studio.core.global.enums.Channels;
import com.studio.core.global.enums.ProductState;
import com.studio.core.global.enums.order.PaymentStatus;
import com.studio.core.global.exception.CustomException;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.order.dto.payment.GetPaymentSuccessResponseDto;
import com.studio.core.order.dto.payment.PaymentCancelRequestDto;
import com.studio.core.order.dto.payment.PaymentRequestDto;
import com.studio.core.order.dto.payment.SaveAmountRequestDto;
import com.studio.core.order.dto.payment.TemPaymentDto;
import com.studio.core.order.entity.OrderEntity;
import com.studio.core.order.entity.payment.PaymentEntity;
import com.studio.core.order.repository.OrderJpaRepository;
import com.studio.core.order.repository.PaymentJpaRepository;
import com.studio.core.product.entity.ProductEntity;
import com.studio.core.product.repository.CartJpaRepository;
import com.studio.core.product.repository.ProductJpaRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final TossPaymentApiService tossPaymentApiService;

    private final ProductJpaRepository productRepository;

    private final PaymentJpaRepository paymentRepository;

    private final CartJpaRepository cartRepository;

    private final OrderJpaRepository orderRepository;

    private final ProductCommonService productCommonService;

    /**
     * 결제 요청전 db에 저장
     *
     * @param req
     * @return
     */
    public TemPaymentDto temPayment(SaveAmountRequestDto req, MemberAuthEntity member) {

        ProductEntity product = productRepository.findById(req.productId())
            .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));

        _checkProductSale(product, req.purchaseQuantity());

        PaymentEntity payment = toPaymentEntity(req, product, member);
        paymentRepository.save(payment);
        return new TemPaymentDto(payment.getChannelProductOrderId(), payment.getTotalAmount());
    }

    public GetPaymentSuccessResponseDto getPayment(String paymentKey) {
        return tossPaymentApiService.getPayment(paymentKey);
    }

    /**
     * 결제 요청 승인
     *
     * @param requestDto
     */
    @Transactional
    public void confirmPayment(PaymentRequestDto requestDto, MemberAuthEntity member) {

        PaymentEntity payment = paymentRepository.findByChannelProductOrderId(requestDto.orderId())
            .orElseThrow(() -> new CustomException(PAYMENT_REQUEST_NOT_FOUND));

        if (!PaymentStatus.READY.equals(payment.getStatus())) {
            throw new CustomException(PAYMENT_CONFIRM_STATUS_NOT_VALID);
        }
        // 결제요청전 금액과 결제승인 금액이 일치하는지 확인
        if (!payment.getTotalAmount().equals(requestDto.amount())) {
            throw new CustomException(PAYMENT_AMOUNT_NOT_MATCH);
        }

        // 결제요청, 결제승인자가 같은지 확인
        if (!payment.getMember().getMemberNo().equals(member.getMemberNo())) {
            throw new CustomException(NOT_VALID_MEMBER);

        }

        Optional<OrderEntity> isExistOrder = orderRepository.findByChannelProductOrderId(
            requestDto.orderId());

        // 주문한 상품인지 확인
        if (isExistOrder.isPresent()) {
            GetPaymentSuccessResponseDto res = tossPaymentApiService.getPayment(
                requestDto.paymentKey());
            payment.updatePayment(res);
            cartRepository.deleteCartByProductAndMember(payment.getProduct().getId(),
                payment.getMember().getMemberNo());
            paymentRepository.save(payment);


        } else {
            _checkProductSale(payment.getProduct(), payment.getPurchaseQuantity());

            // 토스에 결제승인요청
            GetPaymentSuccessResponseDto res = tossPaymentApiService.confirmPayment(requestDto);

            payment.updatePayment(res);

            cartRepository.deleteCartByProductAndMember(payment.getProduct().getId(),
                payment.getMember().getMemberNo());

            OrderEntity order = toOrderEntity(Channels.TRAVELIDGE, payment, payment.getProduct(),
                payment.getMember());

            //주문추가
            orderRepository.save(order);

            // 결제추가
            payment.updateOrder(order);
            paymentRepository.save(payment);

            productCommonService.decreaseProductStock(order.getPurchaseQuantity(),
                payment.getProduct().getId());
        }


    }

    public void cancelPayment(String orderId, String reason) {
        Optional<PaymentEntity> payment = paymentRepository.findByChannelProductOrderId(
            orderId);
        if (payment.isPresent()) {
            PaymentEntity existPayment = payment.get();
            PaymentCancelRequestDto req = new PaymentCancelRequestDto(reason);
            tossPaymentApiService.cancelPayment(existPayment.getPaymentKey(), req);
            
        }

    }

    /**
     * 구매 가능한 상품인지 체그 재고 넘으면 안됨 상품 상태가 판매중이여야됨
     *
     * @param product          상품
     * @param purchaseQuantity 구매수량
     */
    private void _checkProductSale(ProductEntity product, int purchaseQuantity) {

        if (product.getStock() < purchaseQuantity) {
            throw new CustomException(STOCK_OVER);
        }
        if (!ProductState.SALE.equals(product.getProductState())) {
            throw new CustomException(PRODUCT_NOT_VALID_ORDER_STATE);

        }
    }

}
