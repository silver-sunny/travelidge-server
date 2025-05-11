package com.studio.core.global.enums.product.naver;

import com.studio.core.global.enums.ProductState;
import lombok.Getter;

/**
 * 상품 API에서 상품의 판매 상태를 나타내기 위해 사용하는 코드입니다.
 * 상품 등록 시에는 SALE(판매 중)만 입력할 수 있으며,
 * 상품 수정 시에는 SALE(판매 중), SUSPENSION(판매 중지)만 입력할 수 있습니다.
 * StockQuantity의 값이 0인 경우 상품 상태는 OUTOFSTOCK(품절)으로 저장됩니다.
 * 품절 상태의 상품을 판매 중으로 변경할 경우,
 * StockQuantity(재고 수량)와 함께 statusType을 SALE(판매 중)로 입력해야 합니다.
 */
@Getter
public enum NaverProductStatusTypeEnum {

    // 판매대기
    WAIT,
    // 판매중
    SALE,
    // 품정
    OUTOFSTOCK,
    // 승인 대기
    UNADMISSION,
    // 승인 거부
    REJECTION,
    // 판매 중지
    SUSPENSION,
    // 판매 종료
    CLOSE,
    // 판매 금지
    PROHIBITION,
    // 삭제
    DELETE;

    public static NaverProductStatusTypeEnum getNaverProductState(ProductState productStateEnum) {

        switch (productStateEnum) {
            case SALE:
                return NaverProductStatusTypeEnum.SALE;
            case SUSPENSION:
                return NaverProductStatusTypeEnum.SUSPENSION;
        }

        return null;
    }


}

