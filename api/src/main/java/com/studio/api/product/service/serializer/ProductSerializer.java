package com.studio.api.product.service.serializer;

import static com.studio.core.global.utils.ProductUtil.getValueFromDiscountPolicy;

import com.studio.core.global.enums.Channels;
import com.studio.core.global.enums.ProductState;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.product.dto.product.ProductRequestDto;
import com.studio.core.product.dto.product.naver.NaverImagesDto;
import com.studio.core.product.dto.product.naver.NaverOriginProductDto;
import com.studio.core.product.dto.product.naver.NaverProductDto;
import com.studio.core.product.dto.product.naver.NaverProductImageDto;
import com.studio.core.product.entity.ProductEntity;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ProductSerializer {

    public static ProductEntity toProductEntity(ProductRequestDto dto, MemberAuthEntity memberAuth,
        Channels channel) {
        ProductEntity productEntity =  ProductEntity.builder()
            .productName(dto.productName())
            .price(dto.price())
            .discountRate(dto.discountRate())
            .stock(dto.stock())
            .pri(dto.pri())
//            .pdi(dto.pdi().stream().map(pdi -> new ProductImageEntity(pdi)).toList())
            .description(dto.description())
            .channel(channel)
            .registrant(memberAuth)
            .productState(ProductState.SALE)
            .build();

        productEntity.updatePdi(dto.pdi());
        return productEntity;
    }

    public static ProductEntity toProductEntity(Long productId, String channelProductId,
        NaverProductDto requestDto,MemberAuthEntity memberAuth) {

        NaverOriginProductDto naverOriginProduct = requestDto.getOriginProduct();
        NaverImagesDto naverImagesDto = naverOriginProduct.getImages();

        List<NaverProductImageDto> pdi = naverImagesDto.getOptionalImages().stream().toList();
        List<String> pdiStr = pdi.stream().map(NaverProductImageDto::getUrl).toList();

        ProductEntity productEntity = ProductEntity.builder()
            .id(productId)
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
            .registrant(memberAuth)
            .build();

        productEntity.updatePdi(pdiStr);
        return productEntity;
    }


}
