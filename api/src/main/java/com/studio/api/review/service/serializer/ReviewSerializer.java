package com.studio.api.review.service.serializer;


import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.order.entity.OrderEntity;
import com.studio.core.product.entity.ProductEntity;
import com.studio.core.review.dto.ProductReviewRequestDto;
import com.studio.core.review.entity.ReviewEntity;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReviewSerializer {

    public static ReviewEntity toReviewEntity(ProductReviewRequestDto dto, List<String> files,ProductEntity product, OrderEntity order,MemberAuthEntity member) {
        ReviewEntity review =  ReviewEntity.builder()
            .content(dto.content())
            .rating(dto.rating())
            .product(product)
            .order(order)
            .member(member)
            .build();

        review.updateRi(files);
        return review;
    }



}
