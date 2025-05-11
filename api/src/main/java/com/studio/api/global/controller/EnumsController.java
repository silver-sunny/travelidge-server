package com.studio.api.global.controller;

import com.studio.core.global.enums.BanPeriod;
import com.studio.core.global.enums.Channels;
import com.studio.core.global.enums.FilterProductState;
import com.studio.core.global.enums.ProductState;
import com.studio.core.global.enums.inquiry.search.FilterClientInquiry;
import com.studio.core.global.enums.inquiry.search.FilterInquiry;
import com.studio.core.global.enums.inquiry.search.SearchInquiryCondition;
import com.studio.core.global.enums.order.CancelOrReturnState;
import com.studio.core.global.enums.order.ProductOrderState;
import com.studio.core.global.enums.order.search.DateCriteria;
import com.studio.core.global.enums.order.search.SearchCondition;
import com.studio.core.global.enums.order.search.TicketSearchCondition;
import com.studio.core.global.enums.product.naver.CancelReason;
import com.studio.core.global.enums.product.naver.ReturnReason;
import com.studio.core.global.repository.Display;
import com.studio.core.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "Enums controller", description = "enum 묶음")
@RequiredArgsConstructor
@RequestMapping("/v1/api/enums")
public class EnumsController {


    @Operation(summary = "클라상품 문의 내역 필터")
    @GetMapping(value = "/client/inquiry/states")
    public SuccessResponse<?> filterClientInquiryList() {

        return SuccessResponse.ok(FilterClientInquiry.getSearchableStates());

    }

    @Operation(summary = "어드민 상품 문의 내역 필터")
    @GetMapping(value = "/inquiry/states")
    public SuccessResponse<?> filterInquiryList() {

        return SuccessResponse.ok(FilterInquiry.getSearchableStates());

    }


    @Operation(summary = "어드민 상품 문의 내역 상세조건 > 드롭박스")
    @GetMapping(value = "/inquiry/search-condition")
    public SuccessResponse<?> searchInquiryCondition() {

        return SuccessResponse.ok(SearchInquiryCondition.getSearchableStates());

    }



    @Operation(summary = "전체 주문 내역 > 조회기간 > 드롭박스 enum list")
    @GetMapping(value = "/product-order/data-criteria")
    public SuccessResponse<?> dateCriteriaList() {

        return SuccessResponse.ok(DateCriteria.getSearchableStates());

    }

    @Operation(summary = "티켓발급 취소관리 리스트 (미발급 티켓) 조회기간 > 드롭박스 enum list")
    @GetMapping(value = "/product-order/non-ticket/data-criteria")
    public SuccessResponse<?> noneTicketDateCriteriaList() {

        return SuccessResponse.ok(DateCriteria.getNonTicketSearchableStates());

    }


    @Operation(summary = "취소/반품 요청 리스트 > 조회기간 > 드롭박스 enum list")
    @GetMapping(value = "/product-order/return-request/data-criteria")
    public SuccessResponse<?> returnRequestDateCriteriaList() {

        return SuccessResponse.ok(DateCriteria.getReturnRequestSearchableStates());

    }
    @Operation(summary = "상세조건 > 드롭박스 enum list")
    @GetMapping(value = "/ticket/search-condition")
    public SuccessResponse<List<Display>> searchTicketConditions() {

        return SuccessResponse.ok(TicketSearchCondition.getTicketSearchableStates());
    }

    @Operation(summary = "취소/반품 완료 리스트 > 조회기간 > 드롭박스 enum list")
    @GetMapping(value = "/product-order/return-complete/data-criteria")
    public SuccessResponse<?> returnCompleteDateCriteriaList() {

        return SuccessResponse.ok(DateCriteria.getReturnCompleteSearchableStates());

    }

    @Operation(summary = "상세조건 > 드롭박스 enum list")
    @GetMapping(value = "/product-order/search-condition")
    public SuccessResponse<?> searchConditions() {

        return SuccessResponse.ok(SearchCondition.getSearchableStates());

    }

    @Operation(summary = "주문상태 드롭박스 enum list")
    @GetMapping(value = "/product-order/product-order-state")
    public SuccessResponse<?> productOrderStates() {

        return SuccessResponse.ok(ProductOrderState.getSearchableStates());

    }

    @Operation(summary = "취소/반품 상태 enum list")
    @GetMapping(value = "/product-order/cancel-return-state")
    public SuccessResponse<?> cancelOrReturnStates() {

        return SuccessResponse.ok(CancelOrReturnState.getSearchableStates());

    }

    @Operation(summary = "취소 이유 enum list")
    @GetMapping(value = "/product-order/cancel-reason")
    public SuccessResponse<?> getCancelReasonList() {

        return SuccessResponse.ok(CancelReason.getCancelReasons());

    }

    @Operation(summary = "반품 이유 요청 enum list")
    @GetMapping(value = "/product-order/return-reason")
    public SuccessResponse<?> returnReasonList() {

        return SuccessResponse.ok(ReturnReason.getReturnReasons());

    }


    @Operation(summary = "상품 상태 enum list (등록, 수정 페이지)", description = "상품 상태 enum list<br><br>" +
            "상품 등록 또는 수정 페이지 구현 하는데 사용됩니다.")
    @GetMapping(value = "/product/sales/states")
    public SuccessResponse<?> getInsertOrUpdateStates() {

        return SuccessResponse.ok(ProductState.getInsertOrUpdateStates());
    }


    @Operation(summary = "상품 상태 enum list (검색할때 사용)", description = "상품 상태 enum list<br><br>" +
            "상품 등록 또는 수정 페이지 구현 하는데 사용됩니다.")
    @GetMapping(value = "/product/states")
    public SuccessResponse<?> getProductStates() {

        return SuccessResponse.ok(FilterProductState.getFilterProductStates());
    }

    @Operation(summary = "상품 판매 채널 enum list", description = "상품 판매 채널 enum list")
    @GetMapping(value = "/product/channel")
    public SuccessResponse<?> channelList() {

        return SuccessResponse.ok(Channels.getSalesChannelEnumList());
    }


    @Operation(summary = "유저 벤 기간", description = "유저 벤 기간")
    @GetMapping(value = "/user/ban-period")
    public SuccessResponse<?> banPeriod() {

        return SuccessResponse.ok(BanPeriod.getBanPeriod());
    }
}
