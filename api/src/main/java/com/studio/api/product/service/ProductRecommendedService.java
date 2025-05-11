package com.studio.api.product.service;


import com.studio.core.global.enums.Channels;
import java.util.List;
import java.util.stream.Collectors;

import com.studio.api.product.service.serializer.ProductRecommendedSerializer;
import com.studio.core.global.exception.CustomException;
import com.studio.core.global.exception.ErrorCode;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.product.dto.product.GetAdminRecommendedProductResponseDto;
import com.studio.core.product.dto.product.GetClientRecommendedProductResponseDto;
import com.studio.core.product.entity.ProductEntity;
import com.studio.core.product.repository.ProductJpaRepository;
import com.studio.core.product.repository.ProductRecommendedJpaRepository;
import com.studio.core.product.repository.ProductRecommendedQueryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductRecommendedService {
    private final ProductJpaRepository productRepository;
    private final ProductRecommendedJpaRepository jpaRecommendedRepository;

    // 관리자 추천상품 등록
    public void insertRecommendedProduct(Long productId) {
        if (jpaRecommendedRepository.count() >= 20) {
            throw new CustomException(ErrorCode.RECOMMENDED_LIMIT_EXCEEDED);
        }
        if (jpaRecommendedRepository.existsByProductId(productId)) {
            throw new CustomException(ErrorCode.ALREADY_RECOMMENDED);
        }

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        if (product.getChannel() != Channels.TRAVELIDGE) {
            throw new CustomException(ErrorCode.NOT_EXPECT_CHANNEL);
        }

        jpaRecommendedRepository.save(ProductRecommendedSerializer.toEntity(product));
    }

    // 관리자 추천상품 삭제
    public void deleteRecommendedProduct(Long productId) {
        jpaRecommendedRepository.deleteByProductId(productId);
    }

    // 관리자 추천상품 전체 조회
    @Transactional(readOnly = true)
    public List<GetAdminRecommendedProductResponseDto> getAdminRecommendedProducts() {
        return jpaRecommendedRepository.findAllByOrderByIdAsc().stream()
                .map(e -> GetAdminRecommendedProductResponseDto.from(e.getProduct()))
                .collect(Collectors.toList());
    }


}