package com.studio.core.global.naver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "대표 이미지 DTO")
@NoArgsConstructor
public class ImageDto {
    @Schema(description = "이미지 url 대표이미지는 필수, 여러장은 옵션", example = "https://shop-phinf.pstatic.net/20250415_121/1744701009344dwN2u_PNG/78833819491001646_1860902325.png")
    private String url;

    public ImageDto(String url) {
        this.url = url;
    }
}
