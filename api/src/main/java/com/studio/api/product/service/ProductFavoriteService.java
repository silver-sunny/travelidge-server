package com.studio.api.product.service;

import static com.studio.api.product.service.serializer.ProductFavoriteSerializer.toFavoriteEntity;
import com.studio.core.global.exception.CustomException;
import com.studio.core.global.exception.ErrorCode;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.product.entity.ProductEntity;
import com.studio.core.product.entity.ProductFavoriteEntity;
import com.studio.core.product.repository.ProductFavoriteJpaRepository;
import com.studio.core.product.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductFavoriteService {
    private final ProductJpaRepository productRepository;
    private final ProductFavoriteJpaRepository favoriteRepository;

    // 관심상품 등록
    public void insertFavorite(Long productId, MemberAuthEntity member) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        if (favoriteRepository.existsByProductAndMember(product, member)) {
            throw new CustomException(ErrorCode.ALREADY_RECOMMENDED);
        }
        ProductFavoriteEntity favorite = toFavoriteEntity(product, member);
        favoriteRepository.save(favorite);
    }

    // 관심상품 삭제
    @Transactional
    public void deleteFavorite(Long productId, MemberAuthEntity member) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        favoriteRepository.deleteByProductAndMember(product, member);
    }

    // 관심 여부 확인
    public Boolean isFavorite(Long productId, MemberAuthEntity member) {
        ProductEntity product = productRepository.findById(productId)
            .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        return favoriteRepository.existsByProductAndMember(product, member);
    }

    // 관심 수 카운트
    public long getFavoriteCount(Long productId) {
        return favoriteRepository.countById(productId);
    }

}