package com.studio.api.inquiry.service;


import static com.studio.api.inquiry.service.serializer.InquirySerializer.toInquiryEntity;
import static com.studio.core.global.exception.ErrorCode.NOT_VALID_MEMBER;
import static com.studio.core.global.exception.ErrorCode.PRODUCT_NOT_FOUND;

import com.studio.core.global.exception.CustomException;
import com.studio.core.global.exception.ErrorCode;
import com.studio.core.inquiry.dto.AnswerRequestDto;
import com.studio.core.inquiry.dto.InquiryRequestDto;
import com.studio.core.inquiry.dto.ProductInquiryRequestDto;
import com.studio.core.inquiry.entity.InquiryEntity;
import com.studio.core.inquiry.repository.InquiryJpaRepository;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.product.entity.ProductEntity;
import com.studio.core.product.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryJpaRepository inquiryRepository;
    private final ProductJpaRepository productRepository;

    public void insertProductInquiry(Long productId, ProductInquiryRequestDto req, MemberAuthEntity member) {
        ProductEntity product = productRepository.findById(productId).orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));

        InquiryEntity inquiry = toInquiryEntity(req,product,member);
        inquiryRepository.save(inquiry);
    }

    public void insertProductInquiry(InquiryRequestDto req, MemberAuthEntity member) {

        InquiryEntity inquiry = toInquiryEntity(req,member);

        inquiryRepository.save(inquiry);
    }


    public void insertAnswer(Long id, AnswerRequestDto requestDto) {
        InquiryEntity inquiry = inquiryRepository.findById(id).orElseThrow(()-> new CustomException(
            ErrorCode.INQUIRY_NOT_FOUND));

        inquiry.updateAnswer(requestDto.answer());
        inquiryRepository.insertAnswer(inquiry);
    }


    public void deleteInquiry(Long id, MemberAuthEntity member) {

        InquiryEntity inquiry = inquiryRepository.findById(id).orElseThrow(()-> new CustomException(
            ErrorCode.INQUIRY_NOT_FOUND));

        if(!inquiry.getMember().getMemberNo().equals(member.getMemberNo())){
            throw new CustomException(NOT_VALID_MEMBER);
        }

        inquiryRepository.deleteById(id);
    }
}
