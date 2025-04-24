package com.studio.core.product.dto.product.naver;

import com.studio.core.global.enums.product.naver.NaverProductStatusTypeEnum;
import com.studio.core.product.dto.product.ProductRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "네이버 상품 공통 속성 DTO")
@Getter
@NoArgsConstructor
public class NaverOriginProductDto {

    @Schema(description = "상품 판매 상태 코드 | SALE : 판매중 , WAIT : 판매 대기", example = "SALE", defaultValue = "SALE")
    private NaverProductStatusTypeEnum statusType;

    @Schema(description = "상품명", example = "상품명")
    private String name;

    @Schema(description = "상품 상세 정보", example = "상품 상세 정보")
    private String detailContent;

    @Schema(description = "상품 이미지")
    private NaverImagesDto images;

    @Schema(description = "상품 판매 가격 integer <int64> (상품 판매 가격) <= 999999990", example = "10000")
    private Long salePrice;

    @Schema(description = "재고수량 \t\n" +
            "integer <int32> (재고 수량) <= 99999999\n" +
            "상품 등록 시 필수. 상품 수정 시 재고 수량을 입력하지 않으면 스마트스토어 데이터베이스에 저장된 현재 재고 값이 변하지 않습니다. 수정 시 재고 수량이 0으로 입력되면 StatusType으로 전달된 항목은 무시되며 상품 상태는 OUTOFSTOCK(품절)으로 저장됩니다", example = "100")
    private Long stockQuantity;

    @Schema(description = "원상품 상세속성")
    private NaverDetailAttributeDto detailAttribute;

    @Schema(description = "상품 고객 혜택정보")
    private NaverCustomerBenefitDto customerBenefit;

    @Schema(description = "리프 카테고리 ID", example = "50007284", defaultValue = "50007284")
    private String leafCategoryId;

    public NaverOriginProductDto(ProductRequestDto productDto) {
        if (productDto.stock() == 0) {
            this.statusType = NaverProductStatusTypeEnum.OUTOFSTOCK;
        } else {
            this.statusType = NaverProductStatusTypeEnum.getNaverProductState(productDto.productState());

        }


        this.name = productDto.productName();
        this.detailContent = productDto.description();
        this.salePrice = productDto.price();
        this.stockQuantity = productDto.stock();
        this.detailAttribute = new NaverDetailAttributeDto();
        this.leafCategoryId = "50007284";
        this.images = new NaverImagesDto(productDto);
        if (productDto.discountRate() != null && productDto.discountRate() != 0) {
            this.customerBenefit = new NaverCustomerBenefitDto(productDto);
        }

    }



}
