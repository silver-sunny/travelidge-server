package com.studio.core.product.dto.product.naver;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "원상품 상세 속성 DTO")
public class NaverDetailAttributeDto {

    @Schema(description = "A/S 정보")
    private NaverAfterServiceInfoDto afterServiceInfo;

    @Schema(description = "원산지 정보")
    private NaverOriginAreaInfoDto originAreaInfo;

    @Schema(description = "미성년자 구매 가능 여부", defaultValue = "true")
    private boolean minorPurchasable;

    @Schema(description = "상품정보제공고시")
    private NaverProductInfoProvidedNoticeDto productInfoProvidedNotice;

    @Schema(description = "옵션 정보")
    private NaverOptionInfoDto optionInfo;


    public NaverDetailAttributeDto() {
        this.afterServiceInfo = new NaverAfterServiceInfoDto();
        this.originAreaInfo = new NaverOriginAreaInfoDto();
        this.minorPurchasable = true;
        this.productInfoProvidedNotice = new NaverProductInfoProvidedNoticeDto();
        this.optionInfo = new NaverOptionInfoDto();

    }
}
