package com.studio.core.global.enums.product.naver;

import com.studio.core.global.enums.ProductState;
import lombok.Getter;

@Getter
public enum NaverChannelProductDisplayStatusType {

    ON, // 전시중

    SUSPENSION; // 전시 중지


    public static NaverChannelProductDisplayStatusType getNaverChannelProductDisplayStatusTypeEnum(
        ProductState productStateEnum) {

        switch (productStateEnum) {
            case SALE:
                return NaverChannelProductDisplayStatusType.ON;
            case SUSPENSION:
                return NaverChannelProductDisplayStatusType.SUSPENSION;
        }

        return null;
    }


}
