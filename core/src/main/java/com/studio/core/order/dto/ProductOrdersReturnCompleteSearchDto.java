package com.studio.core.order.dto;

import com.studio.core.global.enums.order.search.DateCriteria;
import com.studio.core.global.enums.order.search.SearchCondition;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record ProductOrdersReturnCompleteSearchDto(
    @Schema(description = "조회기간 > 시작날짜") @DateTimeFormat(pattern = "yyyy-M-d") LocalDate startDate,
    @Schema(description = "조회기간 > 끝나는 날짜") @DateTimeFormat(pattern = "yyyy-M-d") LocalDate endDate,// 끝나는 날짜
    @Schema(description = "조회기간 > 드롭박스") DateCriteria dateCriteria, // 조회
    @Schema(description = "상세조건 > 드롭박스") SearchCondition searchCondition,
    @Schema(description = "상세조건 > 검색어") String searchKeyword
) {
}
