package com.studio.core.product.dto.product.naver;

import com.studio.core.global.enums.product.naver.NaverChannelProductDisplayStatusType;
import com.studio.core.product.dto.product.ProductRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "스마트스토어 채널상품 정보 DTO")
@NoArgsConstructor
public class NaverSmartstoreChannelProductDto {

    @Schema(description = "네이버쇼핑 등록 여부")
    private boolean naverShoppingRegistration;

    @Schema(description = "전시 상태 코드 ( 스마트스토어 채널 전용 )")
    private NaverChannelProductDisplayStatusType channelProductDisplayStatusType;


    public NaverSmartstoreChannelProductDto(ProductRequestDto productDto) {
        this.naverShoppingRegistration = true;
        this.channelProductDisplayStatusType = NaverChannelProductDisplayStatusType.getNaverChannelProductDisplayStatusTypeEnum(productDto.productState());
    }

}
