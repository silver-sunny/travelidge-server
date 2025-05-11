package com.studio.core.global.enums.order;

import lombok.Getter;

@Getter
public enum NaverClaimType {
    ETC("없음"),
    CANCEL("취소"),
    RETURN("반품"),
    EXCHANGE("교환"),
    PURCHASE_DECISION_HOLDBACK("구매 확정 보류"),
    ADMIN_CANCEL("직권 취소");

    public final String meaing;

    NaverClaimType(String meaing) {
        this.meaing = meaing;
    }


}
