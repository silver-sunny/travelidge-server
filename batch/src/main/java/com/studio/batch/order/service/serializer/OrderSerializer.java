package com.studio.batch.order.service.serializer;

import static com.studio.core.global.utils.DateUtil.stringDateTimeToLocalDateTimeWithFormat;

import com.studio.core.global.enums.Channels;
import com.studio.core.global.enums.order.CancelOrReturnState;
import com.studio.core.global.enums.order.ProductOrderState;
import com.studio.core.global.enums.order.TicketState;
import com.studio.core.global.naver.dto.NaverCancelDto;
import com.studio.core.global.naver.dto.NaverDataDto;
import com.studio.core.global.naver.dto.NaverOrderInfoDto;
import com.studio.core.global.naver.dto.NaverProductOrderDto;
import com.studio.core.global.naver.dto.NaverReturnDto;
import com.studio.core.order.entity.OrderEntity;
import com.studio.core.product.entity.ProductEntity;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class OrderSerializer {

    public static OrderEntity toOrderEntity(NaverDataDto naverData, ProductEntity product) {
        NaverProductOrderDto npr = naverData.getProductOrder();
        NaverOrderInfoDto noi = naverData.getOrder();
        NaverCancelDto cancelDto = naverData.getCancel();
        NaverReturnDto returnDto = naverData.getReturnDto();

        return OrderEntity.builder()
            .channel(Channels.NAVER)
            .orderState(npr.getProductOrderStatus())
            .product(product)
            .channelOrderId(noi.getOrderId())
            .directOption(Optional.ofNullable(npr.getProductOption())
                .map(option -> option.replace("예약날짜: ", ""))
                .orElse(null))
            .purchaseQuantity(npr.getQuantity())
            .purchasePrice(npr.getTotalPaymentAmount())
            .channelProductOrderId(npr.getProductOrderId())
            .phoneNumber(noi.getOrdererTel())
            .purchaseUserId(noi.getOrdererId())
            .purchaseAt(stringDateTimeToLocalDateTimeWithFormat(noi.getPaymentDate()))
            .shippingDueDate(stringDateTimeToLocalDateTimeWithFormat(npr.getShippingDueDate()))
            .purchaseUserName(noi.getOrdererName())
            .ticketState(TicketState.NON_ISSUED)
            .cancelRequestAt(cancelDto != null ? stringDateTimeToLocalDateTimeWithFormat(cancelDto.getClaimRequestDate()) : null)
            .cancelOrReturnReason(cancelDto != null ? cancelDto.getCancelReason().getMeaning() : null)
            .cancelOrReturnDetailReason(cancelDto != null ? cancelDto.getCancelDetailedReason() : null)
            .cancelOrReturnRequester(cancelDto != null ? cancelDto.getRequestChannel() : null)
            .cancelRejectAt(cancelDto != null && CancelOrReturnState.CANCEL_REJECT.equals(cancelDto.getClaimStatus())
                ? stringDateTimeToLocalDateTimeWithFormat(cancelDto.getCancelCompletedDate())
                : null)
            .cancelDoneAt(cancelDto != null && CancelOrReturnState.CANCEL_DONE.equals(cancelDto.getClaimStatus())
                ? stringDateTimeToLocalDateTimeWithFormat(cancelDto.getCancelCompletedDate())
                : null)
            .returnRequestAt(returnDto != null ? stringDateTimeToLocalDateTimeWithFormat(returnDto.getClaimRequestDate()) : null)
            .cancelOrReturnReason(returnDto != null ? returnDto.getReturnReason().getMeaning() : null)
            .cancelOrReturnDetailReason(returnDto != null ? returnDto.getReturnDetailedReason() : null)
            .cancelOrReturnRequester(returnDto != null ? returnDto.getRequestChannel() : null)
            .returnRejectAt(returnDto != null && CancelOrReturnState.RETURN_REJECT.equals(returnDto.getClaimStatus())
                ? stringDateTimeToLocalDateTimeWithFormat(returnDto.getReturnCompletedDate())
                : null)
            .returnDoneAt(returnDto != null && CancelOrReturnState.RETURN_DONE.equals(returnDto.getClaimStatus())
                ? stringDateTimeToLocalDateTimeWithFormat(returnDto.getReturnCompletedDate())
                : null)
            .cancelOrReturnState(npr.getClaimStatus())
            .build();
    }


}
