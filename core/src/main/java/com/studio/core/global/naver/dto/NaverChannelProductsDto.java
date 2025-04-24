package com.studio.core.global.naver.dto;

import com.studio.core.global.enums.ProductState;
import com.studio.core.global.enums.product.naver.NaverProductChannelServiceType;
import lombok.Getter;

@Getter
public class NaverChannelProductsDto {


    // 원상품 번호
    private Long originProductNo;

    // 채털 상품 번호
    private Long channelProductNo;

    // 채널 서비스 타입
    private String channelServiceType;

    // 카테고리 ID
    private String categoryId;

    // 상품명
    private String name;

    // 상품 판매 상태 코드
    private ProductState statusType;

    // 채털 상품 전시 상태
    private NaverProductChannelServiceType channelProductDisplayStatusType;

    // 판매가
    private Long salePrice;
    // 할인가
    private Long discountedPrice;

    // 모바일 할인가
    private Long mobileDiscountedPrice;

    // 재고 수량
    private Long stockQuantity;

    // 네이버 쇼핑 등록
    private Boolean knowledgeShoppingProductRegistration;

    // 배송 속성
    private String deliveryAttributeType;

    // 기본 배송비
    private Long deliveryFee;

    // 상품 구매 포인트(관리자)
    private Long managerPurchasePoint;

    // 전체 카테고리명
    private String wholeCategoryName;

    // 전체 카테고리 ID
    private String wholeCategoryId;

    // 상품 등록일
    private String regDate;

    // 상품 수정일
    private String modifiedDate;

    // NaverProductChannelServiceTypeEnum에 해당 값이 없을경우 ETC로 세팅
    public void setChannelProductDisplayStatusType(String channelProductDisplayStatusType) {

        try {
            this.channelProductDisplayStatusType = NaverProductChannelServiceType.valueOf(channelProductDisplayStatusType);
        } catch (IllegalArgumentException e) {
            this.channelProductDisplayStatusType = NaverProductChannelServiceType.ETC;
        }
    }

    // ProductStateEnum에 해당 값이 없을경우 ETC로 세팅
    public void setStatusType(String statusType) {
        try {
            this.statusType = ProductState.valueOf(statusType);
        } catch (IllegalArgumentException e) {
            this.statusType = ProductState.ETC;
        }
    }


}
