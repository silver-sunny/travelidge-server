package com.studio.api.product.controller;


import com.studio.api.product.service.ProductService;
import com.studio.core.global.enums.Channels;
import com.studio.core.global.enums.FilterProductState;
import com.studio.core.global.naver.dto.ImagesResponseDto;
import com.studio.core.global.naver.file.FileService;
import com.studio.core.global.response.SuccessResponse;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.product.dto.product.GetProductDetailResponseDto;
import com.studio.core.product.dto.product.GetProductTableResponseDto;
import com.studio.core.product.dto.product.ProductRequestDto;
import com.studio.core.product.repository.ProductQueryJpaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.studio.api.global.service.CommonService.getMemberFromAuth;

@RestController
@RequestMapping("/v1/api/product")
@Tag(name = "product-controller", description = "상품 API")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final ProductQueryJpaRepository productQueryJpaRepository;

    private final FileService fileService;


    @Operation(summary = "상품 등록", description = "상품 등록 <br>")
    @PostMapping
    public SuccessResponse<Void> insertProduct(
        @Validated @RequestBody ProductRequestDto productRequestDto,
        Authentication authentication
    ) {
        MemberAuthEntity member = getMemberFromAuth(authentication);

        productService.insertProduct(productRequestDto, member);
        return SuccessResponse.ok();
    }


    @Operation(summary = "상품 삭제 API", description = "자사 상품 삭제")
    @DeleteMapping("/{productId}")
    @Parameter(name = "productId", description = "삭제 할 상품 번호")
    public SuccessResponse<Void> deleteProduct(@PathVariable Long productId) {

        productService.deleteProduct(productId);
        return SuccessResponse.ok();

    }


    @Operation(summary = "상품 이미지 등록", description = "")
    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SuccessResponse<ImagesResponseDto> uploadNaverProductImages(
        @RequestPart("file") List<MultipartFile> files) {

        return SuccessResponse.ok(fileService.uploadImages(files));
    }


    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetProductTableResponseDto.class)))
    })
    @Operation(summary = "상품 리스트", description = "상품 리스트")
    @GetMapping
    public SuccessResponse<Page<GetProductTableResponseDto>> getProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) FilterProductState state,
        @RequestParam(required = false) Channels channel

        ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        return SuccessResponse.ok(productQueryJpaRepository.getSalesProducts(pageable, state,channel));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetProductDetailResponseDto.class)))
    })
    @Operation(summary = "상품 상세", description = "상품 상세")
    @GetMapping("/{productId}")
    public SuccessResponse<GetProductDetailResponseDto> getProductById(@PathVariable Long productId
    ) {
        return SuccessResponse.ok(productQueryJpaRepository.getProduct(productId));
    }

    @Operation(summary = "상품 수정", description = "상품 수정")
    @PutMapping("/{productId}")
    public SuccessResponse<Void> updateProduct(@PathVariable Long productId,
        @Validated @RequestBody ProductRequestDto dto) {

        productService.updateProduct(productId, dto);
        return SuccessResponse.ok();

    }

}
