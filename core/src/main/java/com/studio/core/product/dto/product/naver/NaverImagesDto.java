package com.studio.core.product.dto.product.naver;

import com.studio.core.product.dto.product.ProductRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "네이버 상품 이미지 DTO")
@NoArgsConstructor
public class NaverImagesDto {

    @Schema(description = "네이버 대표 이미지 DTO")
    private NaverProductImageDto representativeImage;

    @Schema(description = "네이버 추가 이미지 DTO")
    private List<NaverProductImageDto> optionalImages;

    public NaverImagesDto(ProductRequestDto productDto) {
        this.representativeImage = new NaverProductImageDto(productDto.pri());

        List<String> pdi = productDto.pdi();

        if (pdi != null && !pdi.isEmpty()) {
            List<NaverProductImageDto> naverProductImageDtos = new ArrayList<>();

            for(String pdi1 : pdi) {
                NaverProductImageDto pdiImage = new NaverProductImageDto(pdi1);
                naverProductImageDtos.add(pdiImage);
            }
            this.optionalImages = naverProductImageDtos;

        }

    }




}
