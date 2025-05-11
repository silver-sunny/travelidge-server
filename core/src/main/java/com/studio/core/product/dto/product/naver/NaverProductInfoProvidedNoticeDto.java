package com.studio.core.product.dto.product.naver;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "상품 요약 정보 DTO")
public class NaverProductInfoProvidedNoticeDto {

    @Schema(description = "상품정보제공고시 타입", example = "ETC", defaultValue = "ETC")
    private String productInfoProvidedNoticeType;

    @Schema(description = "기타 재화 상품정보제공고시")
    private NaverProductInfoEtcDto etc;

    public NaverProductInfoProvidedNoticeDto(){
        this.productInfoProvidedNoticeType = "ETC";
        this.etc = new NaverProductInfoEtcDto();

    }
}
