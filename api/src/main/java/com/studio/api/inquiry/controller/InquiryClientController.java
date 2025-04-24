package com.studio.api.inquiry.controller;


import static com.studio.api.global.service.CommonService.getMemberFromAuth;

import com.studio.api.inquiry.service.InquiryService;
import com.studio.core.global.enums.inquiry.search.FilterClientInquiry;
import com.studio.core.global.response.SuccessResponse;
import com.studio.core.inquiry.dto.GetClientProductInquiryTableResponse;
import com.studio.core.inquiry.dto.InquiryRequestDto;
import com.studio.core.inquiry.dto.ProductInquiryRequestDto;
import com.studio.core.inquiry.repository.InquiryQueryJpaRepository;
import com.studio.core.member.entity.MemberAuthEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "Inquiry client controller", description = "클라이언트 문의사항")
@RequiredArgsConstructor
@RequestMapping("/v1/api/client/inquiry")
public class InquiryClientController {

    private final InquiryService inquiryService;

    private final InquiryQueryJpaRepository queryJpaRepository;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetClientProductInquiryTableResponse.class)))
    })
    @Operation(summary = "1:1 문의내역 리스트")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("")
    public SuccessResponse<?> getMyInquiryByProductId(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        Authentication authentication) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        MemberAuthEntity member = getMemberFromAuth(authentication);

        return SuccessResponse.ok(
            queryJpaRepository.getClientProductInquiries(null, FilterClientInquiry.MY_INQUIRY,
                member.getMemberNo(), pageable));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetClientProductInquiryTableResponse.class)))
    })
    @Operation(summary = "상품의 문의내역 리스트")
    @GetMapping("/product/{productId}")
    public SuccessResponse<?> getInquiryByProductId(@PathVariable Long productId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) FilterClientInquiry filterClientInquiry,
        Authentication authentication

    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        // 로그인한 사용자인지 확인
        if (authentication == null){
            return SuccessResponse.ok(
                queryJpaRepository.getClientProductInquiries(productId, filterClientInquiry, null,
                    pageable));
        }

        MemberAuthEntity member = getMemberFromAuth(authentication);

        return SuccessResponse.ok(queryJpaRepository.getClientProductInquiries(productId,
            filterClientInquiry,
            member.getMemberNo(), pageable));
    }



    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetClientProductInquiryTableResponse.class)))
    })
    @Operation(summary = "나의 상품 문의내역 리스트")
    @GetMapping("/product")
    public SuccessResponse<?> getInquiryByProductId(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        Authentication authentication

    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        MemberAuthEntity member = getMemberFromAuth(authentication);

        return SuccessResponse.ok(queryJpaRepository.getMyProductInquiries(
            member.getMemberNo(), pageable));
    }
    @Operation(summary = "1:1 문의 하기")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping()
    public SuccessResponse<?> insertInquiry(@Validated @RequestBody InquiryRequestDto requestDto,
        Authentication authentication) {

        MemberAuthEntity member = getMemberFromAuth(authentication);

        inquiryService.insertProductInquiry(requestDto, member);
        return SuccessResponse.ok();

    }


    @Operation(summary = "상품 문의")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/product/{productId}")
    public SuccessResponse<?> insertProductInquiry(@PathVariable Long productId,
        @Validated @RequestBody ProductInquiryRequestDto requestDto,
        Authentication authentication) {
        MemberAuthEntity member = getMemberFromAuth(authentication);

        inquiryService.insertProductInquiry(productId, requestDto, member);
        return SuccessResponse.ok();

    }


    @Operation(summary = "문의 삭제")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @DeleteMapping("/{id}")
    public SuccessResponse<?> deleteInquiry(@PathVariable Long id,
        Authentication authentication) {
        MemberAuthEntity member = getMemberFromAuth(authentication);

        inquiryService.deleteInquiry(id, member);
        return SuccessResponse.ok();

    }


}
