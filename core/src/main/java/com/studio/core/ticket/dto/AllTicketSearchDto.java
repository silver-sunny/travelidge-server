package com.studio.core.ticket.dto;

import com.studio.core.global.enums.order.search.TicketSearchCondition;
import io.swagger.v3.oas.annotations.media.Schema;

public record AllTicketSearchDto(
        @Schema(description = "상세조건 > 드롭박스") TicketSearchCondition searchCondition,
        @Schema(description = "상세조건 > 검색어") String searchKeyword
) {
}
