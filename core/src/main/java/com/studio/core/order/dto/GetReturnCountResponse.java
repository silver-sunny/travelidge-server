package com.studio.core.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "취소/반품 요청, 완료 갯수")
public record GetReturnCountResponse(
        @Schema(description = "취소/반품 요청 수")
        int requestCount,

        @Schema(description = "취소/반품 완료 수")
        int completeCount) {

}
