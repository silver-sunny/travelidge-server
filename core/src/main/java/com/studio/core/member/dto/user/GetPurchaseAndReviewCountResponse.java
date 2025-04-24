package com.studio.core.member.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "구매갯수, 작성가능한 후기 갯수")
public record GetPurchaseAndReviewCountResponse(
        @Schema(description = "구매 갯수 요청 수")
        Long purchaseCount,

        @Schema(description = "작성가능한 후기 수")
        Long reviewCount) {

}
