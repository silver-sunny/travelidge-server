package com.studio.core.inquiry.dto.search;

import com.studio.core.global.enums.inquiry.search.FilterInquiry;
import com.studio.core.global.enums.inquiry.search.SearchInquiryCondition;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record InquirySearchDto(
    @Schema(description = "답변필터 > 드롭박스") FilterInquiry filterInquiry,
    @Schema(description = "문의한 검색 시작날짜") @DateTimeFormat(pattern = "yyyy-M-d") LocalDate startDate,
    @Schema(description = "문의한 검색 끝나는 날짜") @DateTimeFormat(pattern = "yyyy-M-d") LocalDate endDate
) {

}
