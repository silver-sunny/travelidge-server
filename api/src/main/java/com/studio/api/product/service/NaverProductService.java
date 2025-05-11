package com.studio.api.product.service;


import static com.studio.api.product.service.serializer.ProductSerializer.toProductEntity;
import static com.studio.core.global.exception.ErrorCode.NOT_EXPECT_CHANNEL;
import static com.studio.core.global.exception.ErrorCode.PRODUCT_NOT_FOUND;
import static com.studio.core.global.exception.ErrorCode.PRODUCT_NOT_VALID_STATE;

import com.studio.core.global.enums.Channels;
import com.studio.core.global.enums.ProductState;
import com.studio.core.global.exception.CustomException;
import com.studio.core.global.naver.service.NaverProductApiService;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.product.dto.product.ProductRequestDto;
import com.studio.core.product.dto.product.naver.NaverProductDto;
import com.studio.core.product.dto.product.naver.NaverProductResponseDto;
import com.studio.core.product.entity.ProductEntity;
import com.studio.core.product.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NaverProductService {

    private final NaverProductApiService naverProductApiService;

    private final ProductJpaRepository productRepository;


    /**
     * 네이버 상품 등록
     *
     * @param productRequestDto
     */
    public NaverProductDto insertNaverProduct(ProductRequestDto productRequestDto, MemberAuthEntity member) {

        NaverProductDto naverProductDto = new NaverProductDto(productRequestDto);

        // 등록된적이 없으면
        // 처음 등록인데 판매중이면 insert
        NaverProductResponseDto naverProductResponseDto = naverProductApiService.registerProduct(
            naverProductDto);

        // 자체 db에 저장
        return getNaverProductInfoAndInsert(naverProductResponseDto, member);

    }


    public NaverProductDto updateNaverProduct(Long productId, ProductRequestDto dto) {

        ProductEntity product = productRepository.findById(productId)
            .orElseThrow(() -> new CustomException(
                PRODUCT_NOT_FOUND));
        if (!Channels.NAVER.equals(product.getChannel())) {
            throw new CustomException(NOT_EXPECT_CHANNEL);
        }

        if (ProductState.isModifiable(product.getProductState())) {
            NaverProductDto naverProductDto = new NaverProductDto(dto);
            naverProductApiService.updateProduct(product.getChannelProductId(), naverProductDto);

            NaverProductDto naverProductRes = naverProductApiService.getNaverProduct(
                product.getChannelProductId());

            product.updateProduct(naverProductRes);

            productRepository.save(product);

            return naverProductRes;
        } else {
            throw new CustomException(PRODUCT_NOT_VALID_STATE);
        }


    }

    /**
     * 네이버 상품 가져와 db에 저장
     *
     * @param naverProductResponseDto
     * @return
     */
    public NaverProductDto getNaverProductInfoAndInsert(
        NaverProductResponseDto naverProductResponseDto, MemberAuthEntity member) {

        String naverProductNo = String.valueOf(
            naverProductResponseDto.getSmartstoreChannelProductNo());
        NaverProductDto naverProductRes = naverProductApiService.getNaverProduct(naverProductNo);

        ProductEntity product = toProductEntity(null, naverProductNo, naverProductRes, member);
        // 판매 채널: 자사 상품 등록
        productRepository.save(product);
        return naverProductRes;
    }


    public void deleteNaverProduct(Long productId) {

        ProductEntity product = productRepository.findById(productId)
            .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));

        if (!Channels.NAVER.equals(product.getChannel())) {
            throw new CustomException(NOT_EXPECT_CHANNEL);
        }

        // 네이버 API 에서 상품 삭제
        naverProductApiService.deleteNaverProduct(product.getChannelProductId());

        // 자체 db에서 삭제
        product.updateProductState(ProductState.DELETE);


    }

}
