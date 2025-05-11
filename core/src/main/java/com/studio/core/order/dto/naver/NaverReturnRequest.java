package com.studio.core.order.dto.naver;

import com.studio.core.global.enums.product.naver.ReturnReason;
import lombok.Getter;

@Getter
public class NaverReturnRequest {

    private ReturnReason returnReason;

    private String collectDeliveryMethod;

    public NaverReturnRequest(ReturnReason returnReason) {
        this.returnReason = returnReason;
        this.collectDeliveryMethod = "NOTHING";
    }
}
