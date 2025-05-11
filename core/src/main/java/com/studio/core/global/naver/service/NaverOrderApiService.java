package com.studio.core.global.naver.service;

import static com.studio.core.global.constant.naver.NaverProductUrlConstant.GET_NAVER_PRODUCT_ORDERS_URL;
import static com.studio.core.global.constant.naver.NaverProductUrlConstant.GET_NAVER_PRODUCT_ORDER_URL;
import static com.studio.core.global.constant.naver.NaverProductUrlConstant.NAVER_DISPATCH_URL;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studio.core.global.naver.dto.NaverOrderStatusDto;
import com.studio.core.global.naver.dto.NaverProductOrderIdsDto;
import com.studio.core.global.naver.dto.NaverProductOrderInfo;
import com.studio.core.order.dto.naver.NaverDisPatchProductOrderInfo;
import com.studio.core.order.dto.naver.NaverDispatchProductOrderDto;
import com.studio.core.order.dto.naver.NaverDispatchProductOrdersDto;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NaverOrderApiService {

    private final NaverApiService naverApiService;

    public NaverOrderStatusDto getNaverProductOrder(String searchDate) {

        String url =
            GET_NAVER_PRODUCT_ORDER_URL + "?lastChangedFrom=" + URLEncoder.encode(searchDate,
                StandardCharsets.UTF_8) + "&limitCount=300";

        return naverApiService.sendRequest(
            url,
            HttpMethod.GET,
            null,
            NaverOrderStatusDto.class
        ).block();
//        return naverApiService.sendRequest(
//            GET_NAVER_PRODUCT_ORDER_URL + "?lastChangedFrom=" + URLEncoder.encode(searchDate,
//                StandardCharsets.UTF_8) + "&limitCount=300", HttpMethod.GET, null,
//            NaverOrderStatusDto.class).block();

    }


    public NaverProductOrderInfo getProductOrders(NaverProductOrderIdsDto naverProductOrderIds) {

        return naverApiService.sendRequest(GET_NAVER_PRODUCT_ORDERS_URL, HttpMethod.POST,
            naverProductOrderIds, NaverProductOrderInfo.class).block();

    }


    public NaverDisPatchProductOrderInfo dispatchNaverProductOrder(String channelProductOrderId) {
        NaverDispatchProductOrderDto dispatchProductOrderDto = new NaverDispatchProductOrderDto(
            channelProductOrderId);
        List<NaverDispatchProductOrderDto> dispatchProductOrderDtoList = new ArrayList<>();
        dispatchProductOrderDtoList.add(dispatchProductOrderDto);
        NaverDispatchProductOrdersDto dispatchProductOrdersDto = new NaverDispatchProductOrdersDto(
            dispatchProductOrderDtoList);

        return naverApiService.sendRequest(NAVER_DISPATCH_URL, HttpMethod.POST,
            dispatchProductOrdersDto, NaverDisPatchProductOrderInfo.class).block();


    }
}
