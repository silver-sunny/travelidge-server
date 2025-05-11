package com.studio.batch.product.processor;

import static com.studio.batch.product.serializer.ProductSerializer.toProductEntity;
import static com.studio.core.global.constant.naver.NaverProductUrlConstant.NAVER_GET_PRODUCT_URL;

import com.studio.core.global.naver.dto.NaverChannelProductsDto;
import com.studio.core.global.naver.service.NaverApiService;
import com.studio.core.global.naver.service.NaverProductApiService;
import com.studio.core.product.dto.product.naver.NaverProductDto;
import com.studio.core.product.entity.ProductEntity;
import com.studio.core.product.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class NaverProductProcessor implements
    ItemProcessor<NaverChannelProductsDto, ProductEntity> {

    private final ProductJpaRepository productRepository;
    private final NaverProductApiService naverApiService;

    @Override
    public ProductEntity process(NaverChannelProductsDto items) {
        return productRepository.findByChannelProductId(String.valueOf(items.getChannelProductNo()))
            .map(product -> {
                product.updateProduct(items); // 존재하면 업데이트
                return product;
            })
            .orElse(getNaverProduct(items.getChannelProductNo())); // 없으면 null 반환
    }

    public ProductEntity getNaverProduct(Long channelProductNo) {
        String naverChannelProduct = String.valueOf(channelProductNo);
        NaverProductDto naverProduct = naverApiService.getNaverProduct(naverChannelProduct);
        return toProductEntity(naverChannelProduct, naverProduct);


    }

}
