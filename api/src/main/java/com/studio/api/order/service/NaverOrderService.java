package com.studio.api.order.service;

import static com.studio.core.global.constant.naver.NaverProductUrlConstant.GET_NAVER_PRODUCT_ORDERS_URL;
import static com.studio.core.global.constant.naver.NaverProductUrlConstant.NAVER_CANCEL_APPROVE_URL;
import static com.studio.core.global.constant.naver.NaverProductUrlConstant.NAVER_CANCEL_REQUEST_URL;
import static com.studio.core.global.constant.naver.NaverProductUrlConstant.NAVER_RETURN_APPROVE_URL;
import static com.studio.core.global.constant.naver.NaverProductUrlConstant.NAVER_RETURN_REJECT_URL;
import static com.studio.core.global.constant.naver.NaverProductUrlConstant.NAVER_RETURN_REQUEST_URL;
import static com.studio.core.global.exception.ErrorCode.IS_PROGRESSING;
import static com.studio.core.global.exception.ErrorCode.NOT_EXPECT_CHANNEL;
import static com.studio.core.global.exception.ErrorCode.NOT_VALID_STATE;
import static com.studio.core.global.exception.ErrorCode.ORDER_NOT_FOUND;
import static com.studio.core.global.utils.CommonUil.toJson;

import com.studio.core.global.enums.Channels;
import com.studio.core.global.enums.product.naver.CancelReason;
import com.studio.core.global.enums.product.naver.ReturnReason;
import com.studio.core.global.exception.CustomException;
import com.studio.core.global.naver.dto.NaverDataDto;
import com.studio.core.global.naver.dto.NaverProductOrderIdsDto;
import com.studio.core.global.naver.dto.NaverProductOrderInfo;
import com.studio.core.global.naver.service.NaverApiService;
import com.studio.core.order.dto.naver.NaverCancelReasonRequest;
import com.studio.core.order.dto.naver.NaverRejectReturnReason;
import com.studio.core.order.dto.naver.NaverReturnRequest;
import com.studio.core.order.entity.OrderEntity;
import com.studio.core.order.repository.OrderJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverOrderService {

    private final OrderJpaRepository orderRepository;
    private final NaverApiService naverApiService;

    /**
     * 네이버 주문 반품 요청 및 승인
     */
    public void naverReturnRequestAndApprove(Long orderId, ReturnReason returnReason) {
        OrderEntity order = getProductOrder(orderId);

        naverApiService.sendRequest(
            String.format(NAVER_RETURN_REQUEST_URL, order.getChannelProductOrderId()),
            HttpMethod.POST, new NaverReturnRequest(returnReason), Void.class);
        naverApiService.sendRequest(
            String.format(NAVER_RETURN_APPROVE_URL, order.getChannelProductOrderId()),
            HttpMethod.POST, null, Void.class);

        updateProductOrderState(order);
    }

    /**
     * 네이버 주문 취소
     */
    public void naverCancelRequest(Long orderId, CancelReason cancelReason) {
        OrderEntity order = getProductOrder(orderId);

        naverApiService.sendRequest(
            String.format(NAVER_CANCEL_REQUEST_URL, order.getChannelProductOrderId()),
            HttpMethod.POST,
            new NaverCancelReasonRequest(cancelReason), Void.class);

        updateProductOrderState(order);

    }


    /**
     * 반품 철회(거부)
     *
     * @param orderId
     */
    public void naverReturnReject(Long orderId, String rejectReturnReason) {

        OrderEntity order = getProductOrder(orderId);

        String requestBody = toJson(new NaverRejectReturnReason(rejectReturnReason));

        naverApiService.sendRequest(
            String.format(NAVER_RETURN_REJECT_URL, order.getChannelProductOrderId()),
            HttpMethod.POST,
            new NaverRejectReturnReason(rejectReturnReason), Void.class);
        updateProductOrderState(order);

    }

    /**
     * 취소, 반품 승인
     * @param orderId
     */
    public void naverCancelOrReturnApprove(Long orderId) {
        OrderEntity order = getProductOrder(orderId);

        switch (order.getCancelOrReturnState()) {
            case CANCEL_REQUEST -> naverApiService.sendRequest(
                String.format(NAVER_CANCEL_APPROVE_URL, order.getChannelProductOrderId()),
                HttpMethod.POST,
                null, Void.class);

            case RETURN_REQUEST -> naverApiService.sendRequest(
                String.format(NAVER_RETURN_APPROVE_URL, order.getChannelProductOrderId()),
                HttpMethod.POST,
                null, Void.class);

            default -> throw new CustomException(NOT_VALID_STATE,
                " > 취소, 반품 요청상태가 없습니다.: " + order.getCancelOrReturnState());
        }
        updateProductOrderState(order);
    }


    /**
     * 주문번호로 반품 요청
     *
     * @param orderId
     * @param returnReason
     */
    public void naverReturnRequest(Long orderId, ReturnReason returnReason) {

        OrderEntity order = getProductOrder(orderId);

        String requestBody = toJson(new NaverReturnRequest(returnReason));

        naverApiService.sendRequest(NAVER_RETURN_REQUEST_URL + order.getChannelProductOrderId(),
            HttpMethod.POST,
            new NaverReturnRequest(returnReason), Void.class);

        updateProductOrderState(order);

    }

    /**
     * 공통 - 주문 조회 및 검증
     */
    private OrderEntity getProductOrder(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(
            ORDER_NOT_FOUND));
        String channelProductOrderId = order.getChannelProductOrderId();

        if (StringUtils.isEmpty(channelProductOrderId) ||
            !order.getChannel().equals(Channels.NAVER)) {

            // channelProductOrderId 없을경우 , 네이버가 아닐경우 에러
            throw new CustomException(NOT_EXPECT_CHANNEL);
        }
        if (order.isProgressing()) {
            throw new CustomException(IS_PROGRESSING);

        }
        return order;
    }


    /**
     * 네이버 상품 주문 업데이트
     *
     * @param order
     */
    private void updateProductOrderState(OrderEntity order) {

        String requestBody = toJson(
            new NaverProductOrderIdsDto(List.of(order.getChannelProductOrderId())));

        NaverProductOrderInfo naverProductOrderInfo = naverApiService.sendRequest(
                GET_NAVER_PRODUCT_ORDERS_URL, HttpMethod.POST, requestBody, NaverProductOrderInfo.class)
            .block();

        List<NaverDataDto> naverDataDtos = naverProductOrderInfo.getData();

        for (NaverDataDto naverDataDto : naverDataDtos) {
            order.updateOrder(naverDataDto);

            orderRepository.save(order);
        }

    }

}