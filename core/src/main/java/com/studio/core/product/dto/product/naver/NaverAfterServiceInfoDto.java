package com.studio.core.product.dto.product.naver;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "A/S 정보DTO")
@Getter

public class NaverAfterServiceInfoDto {

    @Schema(description = "A/S 전화번호", example = "061-1111-2222")
    private String afterServiceTelephoneNumber;

    @Schema(description = "A/S 안내", example = "as안내입니다,")
    private String afterServiceGuideContent;

    public NaverAfterServiceInfoDto(){
        this.afterServiceTelephoneNumber = "061-1111-2222";
        this.afterServiceGuideContent = "as안내입니다";
    }
}
