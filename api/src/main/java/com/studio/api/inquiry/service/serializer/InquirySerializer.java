package com.studio.api.inquiry.service.serializer;

import com.studio.core.global.enums.inquiry.InquiryPrivateState;
import com.studio.core.global.enums.inquiry.InquiryResolvedState;
import com.studio.core.inquiry.dto.InquiryRequestDto;
import com.studio.core.inquiry.dto.ProductInquiryRequestDto;
import com.studio.core.inquiry.entity.InquiryEntity;

import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.product.entity.ProductEntity;
import org.springframework.stereotype.Service;

@Service
public class InquirySerializer {

    public static InquiryEntity toInquiryEntity(
        ProductInquiryRequestDto dto,ProductEntity product, MemberAuthEntity memberAuth) {
        return InquiryEntity.builder()
            .inquiry(dto.inquiry())
            .isPrivate(InquiryPrivateState.fromBoolean(dto.isPrivate()))
            .product(product)
            .member(memberAuth)
            .isResolved(InquiryResolvedState.NOT_RESOLVED)
            .build();

    }

    public static InquiryEntity toInquiryEntity(InquiryRequestDto dto,MemberAuthEntity memberAuth) {
        return InquiryEntity.builder()
            .isPrivate(InquiryPrivateState.PRIVATE)
            .isResolved(InquiryResolvedState.NOT_RESOLVED)
            .inquiry(dto.inquiry())
            .member(memberAuth)
            .build();

    }


}
