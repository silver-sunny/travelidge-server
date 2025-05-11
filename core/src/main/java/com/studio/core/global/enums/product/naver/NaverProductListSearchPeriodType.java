package com.studio.core.global.enums.product.naver;

import lombok.Getter;

@Getter
public enum NaverProductListSearchPeriodType {


    PROD_REG_DAY("상품 등록일"),
    SALE_START_DAY("판매 시작일"),
    SALE_END_DAY("판매 종료일"),
    PROD_MOD_DAY("최종 수정일");

    public final String meaing;

    NaverProductListSearchPeriodType(String meaing) {
        this.meaing = meaing;
    }


}
