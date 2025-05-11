package com.studio.api.inquiry.controller;


import com.studio.api.inquiry.service.InquiryService;
import com.studio.core.global.enums.inquiry.search.FilterClientInquiry;
import com.studio.core.global.response.SuccessResponse;
import com.studio.core.inquiry.dto.AnswerRequestDto;
import com.studio.core.inquiry.dto.GetInquiryTableResponse;
import com.studio.core.inquiry.dto.GetProductInquiryTableResponse;
import com.studio.core.inquiry.dto.search.InquirySearchDto;
import com.studio.core.inquiry.dto.search.ProductInquirySearchDto;
import com.studio.core.inquiry.repository.InquiryQueryJpaRepository;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "Inquiry controller", description = "어드민 문의사항")
@RequiredArgsConstructor
@RequestMapping("/v1/api/inquiry")
public class InquiryController {

    private final InquiryService inquiryService;

    private final InquiryQueryJpaRepository queryJpaRepository;


    @Operation(summary = "문의 답변")
    @PreAuthorize("hasAnyAuthority('ROLE_ROOT','ROLE_MANAGE','ROLE_GUEST')")
    @PutMapping("/{id}/reply")
    public SuccessResponse<?> insertAnswer(@PathVariable Long id,
        @Validated @RequestBody AnswerRequestDto requestDto) {
        inquiryService.insertAnswer(id, requestDto);
        return SuccessResponse.ok();

    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetProductInquiryTableResponse.class)))
    })
    @Operation(summary = "상품 문의내역 리스트")
    @GetMapping("/product")
    public SuccessResponse<?> getInquiryByProductId(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @ModelAttribute ProductInquirySearchDto searchDto
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        return SuccessResponse.ok(
            queryJpaRepository.getProductInquiries(searchDto, pageable));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetInquiryTableResponse.class)))
    })
    @Operation(summary = "1:1 문의내역 리스트")
    @GetMapping("")
    public SuccessResponse<?> getInquiry(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @ModelAttribute InquirySearchDto searchDto) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        return SuccessResponse.ok(
            queryJpaRepository.getInquiries(searchDto, pageable));
    }
}
