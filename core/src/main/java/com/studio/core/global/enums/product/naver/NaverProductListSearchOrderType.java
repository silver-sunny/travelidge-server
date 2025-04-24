package com.studio.core.global.enums.product.naver;

import lombok.Getter;

@Getter
public enum NaverProductListSearchOrderType {


    NO("상품 번호순"),
    NAME("상품명순"),
    REG_DATE("등록일순"),
    MOD_DATE("최종 수정일 순");

    public final String meaing;

    NaverProductListSearchOrderType(String meaing) {
        this.meaing = meaing;
    }


}
