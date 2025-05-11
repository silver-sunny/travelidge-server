package com.studio.core.global.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


public class CommonResponse {

    @Getter
    @Setter
    public abstract static class BaseTime {
        @Schema(description = "생성일자", example = "2023-10-30T06:55:16.812Z")
        private LocalDateTime createdAt;
        @Schema(description = "업데이트일자", example = "2023-10-30T06:55:16.812Z")
        private LocalDateTime updatedAt;
    }


}

