package com.studio.core.product.dto.product.naver;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "대표 이미지 DTO")
@NoArgsConstructor
public class NaverProductImageDto {

    @Schema(description = "이미지 url 대표이미지는 필수, 여러장은 옵션", example = "https://shop-phinf.pstatic.net/20250415_293/1744701051585ibKTu_PNG/38639399723298094_1233920217.png")
    private String url;

    public NaverProductImageDto(String url) {
        this.url = url;
    }
}
