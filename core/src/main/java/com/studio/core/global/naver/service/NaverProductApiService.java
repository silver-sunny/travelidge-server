package com.studio.core.global.naver.service;

import static com.studio.core.global.constant.naver.NaverProductUrlConstant.*;

import com.studio.core.global.enums.product.naver.NaverProductListSearchOrderType;
import com.studio.core.global.enums.product.naver.NaverProductListSearchPeriodType;
import com.studio.core.global.naver.dto.NaverProductSearchRequestDto;
import com.studio.core.global.naver.dto.NaverProductsResponseDto;
import com.studio.core.global.utils.DateUtil;
import com.studio.core.product.dto.product.naver.NaverProductDto;
import com.studio.core.product.dto.product.naver.NaverProductResponseDto;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverProductApiService {
    private final NaverApiService naverApiService;



    /**
     * 네이버 API에서 상품 하나 가져오기
     */
    public NaverProductDto getNaverProduct(String channelProductNo) {
        return naverApiService.sendRequest(NAVER_GET_PRODUCT_URL + channelProductNo, HttpMethod.GET, null, NaverProductDto.class).block();
    }


    public NaverProductsResponseDto getNaverProducts(String searchDate, int currentPage) {


        // 네이버 커머스 검색 요청 DTO 생성
        NaverProductSearchRequestDto requestDto = NaverProductSearchRequestDto.builder()
            .page(currentPage)
            .fromDate(searchDate == null ? DateUtil.getCurrentDateAsString() : searchDate)
            .orderType(NaverProductListSearchOrderType.MOD_DATE)
            .size(500)
            .periodType(NaverProductListSearchPeriodType.PROD_MOD_DAY)
            .build();

        return naverApiService.sendRequest(GET_NAVER_PRODUCTS_SEARCH_URL, HttpMethod.POST, requestDto, NaverProductsResponseDto.class).block();

    }

    /**
     * 네이버 API에서 상품 삭제
     */
    public void deleteNaverProduct(String channelProductNo) {
        naverApiService.sendRequest(NAVER_DELETE_PRODUCT_URL + channelProductNo, HttpMethod.DELETE, null, Void.class);
    }

    /**
     * 네이버 API에서 상품 등록
     */
    public NaverProductResponseDto registerProduct(NaverProductDto productRequest) {
        return naverApiService.sendRequest(NAVER_INSERT_PRODUCT_URL, HttpMethod.POST, productRequest, NaverProductResponseDto.class).block();
    }

    /**
     * 네이버 API에서 상품 수정
     */
    public NaverProductResponseDto updateProduct(String channelProductNo, NaverProductDto productRequest) {
        return naverApiService.sendRequest(NAVER_UPDATE_PRODUCT_URL + channelProductNo, HttpMethod.PUT, productRequest, NaverProductResponseDto.class).block();
    }

}
