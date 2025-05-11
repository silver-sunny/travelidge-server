package com.studio.core.global.enums.product.naver;

import com.studio.core.global.enums.ProductState;
import lombok.Getter;

@Getter
public enum NaverMethodStateEnum {

    INSERT, INSERT_AND_UPDATE;

    public static NaverMethodStateEnum getNaverState(ProductState productStateEnum) {

        // 자체 등록이 판매중이면 추가
        if (ProductState.SALE.equals(productStateEnum)) {
            return NaverMethodStateEnum.INSERT;
        } else {
            // 대기중이면 insert 후 update
            return NaverMethodStateEnum.INSERT_AND_UPDATE;
        }
    }
}
