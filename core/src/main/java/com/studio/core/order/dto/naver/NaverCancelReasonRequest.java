package com.studio.core.order.dto.naver;

import com.studio.core.global.enums.product.naver.CancelReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NaverCancelReasonRequest {

    private CancelReason cancelReason;

}
