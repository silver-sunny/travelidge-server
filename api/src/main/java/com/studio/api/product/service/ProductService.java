package com.studio.api.product.service;


import static com.studio.api.product.service.serializer.ProductSerializer.toProductEntity;
import static com.studio.core.global.exception.ErrorCode.NOT_EXPECT_CHANNEL;
import static com.studio.core.global.exception.ErrorCode.PRODUCT_NOT_FOUND;

import com.studio.core.global.enums.Channels;
import com.studio.core.global.enums.ProductState;
import com.studio.core.global.exception.CustomException;
import com.studio.core.inquiry.repository.InquiryJpaRepository;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.product.dto.product.GetProductReviewInquiryCountResponseDto;
import com.studio.core.product.dto.product.ProductRequestDto;
import com.studio.core.product.entity.ProductEntity;
import com.studio.core.product.repository.CartJpaRepository;
import com.studio.core.product.repository.ProductFavoriteJpaRepository;
import com.studio.core.product.repository.ProductJpaRepository;
import com.studio.core.product.repository.ProductRecommendedJpaRepository;
import com.studio.core.review.repository.ReviewJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductJpaRepository productRepository;
    private final CartJpaRepository cartRepository;
    private final ReviewJpaRepository reviewRepository;

    private final ProductRecommendedJpaRepository recommendedRepository;
    private final ProductFavoriteJpaRepository favoriteRepository;

    private final InquiryJpaRepository inquiryRepository;

    public void insertProduct(ProductRequestDto dto, MemberAuthEntity member) {

        ProductEntity product = toProductEntity(dto, member, Channels.TRAVELIDGE);

        productRepository.save(product);
    }


    public void deleteProduct(Long productId) {

        // 해당 상품의 대한 주문 내역이 있을 경우 삭제 불가능
//        if (productOrderService.isExistOrderByProductNo(productNo)) {
//            throw new ConditionFailException(ProductOrderConstant.PRODUCT_IS_EXIST_ORDER);
//        }

        ProductEntity product = productRepository.findById(productId)
            .orElseThrow(() -> new CustomException(
                PRODUCT_NOT_FOUND));

        // 자체만 삭제 가능
        if (!product.getChannel().equals(Channels.TRAVELIDGE)) {
            throw new CustomException(NOT_EXPECT_CHANNEL);
        }

        product.updateProductState(ProductState.DELETE);
        productRepository.save(product);
        cartRepository.deleteByProductId(productId);
        recommendedRepository.deleteByProductId(productId);
        favoriteRepository.deleteByProductId(productId);
    }


    public void updateProduct(Long productId, ProductRequestDto dto) {

        ProductEntity product = productRepository.findById(productId)
            .orElseThrow(() -> new CustomException(
                PRODUCT_NOT_FOUND));
        // 자체만 삭제 가능
        if (!product.getChannel().equals(Channels.TRAVELIDGE)) {
            throw new CustomException(NOT_EXPECT_CHANNEL);
        }

        product.updateProduct(dto);
        productRepository.save(product);
    }

    public GetProductReviewInquiryCountResponseDto getReviewInquiryCount(Long productId) {

        int inquiryCount = inquiryRepository.countByProductId(productId);
        int reviewCount = reviewRepository.countByProductId(productId);
        return new GetProductReviewInquiryCountResponseDto(reviewCount, inquiryCount);
    }

}
