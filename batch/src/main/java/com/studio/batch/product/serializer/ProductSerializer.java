package com.studio.batch.product.serializer;

import static com.studio.core.global.utils.ProductUtil.getValueFromDiscountPolicy;

import com.studio.core.global.enums.Channels;
import com.studio.core.global.enums.ProductState;
import com.studio.core.product.dto.product.naver.NaverImagesDto;
import com.studio.core.product.dto.product.naver.NaverOriginProductDto;
import com.studio.core.product.dto.product.naver.NaverProductDto;
import com.studio.core.product.dto.product.naver.NaverProductImageDto;
import com.studio.core.product.entity.ProductEntity;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ProductSerializer {



    public static ProductEntity toProductEntity(String channelProductId,
        NaverProductDto requestDto) {

        NaverOriginProductDto naverOriginProduct = requestDto.getOriginProduct();
        NaverImagesDto naverImagesDto = naverOriginProduct.getImages();


        List<NaverProductImageDto> pdi = naverImagesDto.getOptionalImages().stream().toList();
        List<String> pdiStr = pdi.stream().map(NaverProductImageDto::getUrl).toList();

        ProductEntity productEntity = ProductEntity.builder()
            .productName(naverOriginProduct.getName())
            .price(naverOriginProduct.getSalePrice())
            .discountRate(getValueFromDiscountPolicy(naverOriginProduct.getCustomerBenefit()))
            .stock(naverOriginProduct.getStockQuantity())
            .productState(ProductState.getProductStateByNaverProductStatusEnum(
                naverOriginProduct.getStatusType()))
            .pri(naverImagesDto.getRepresentativeImage().getUrl())
            .description(naverOriginProduct.getDetailContent())
            .channel(Channels.NAVER)
            .channelProductId(channelProductId)
            .build();

        productEntity.updatePdi(pdiStr);
        return productEntity;
    }


}
