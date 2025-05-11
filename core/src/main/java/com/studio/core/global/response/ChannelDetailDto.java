package com.studio.core.global.response;

import com.studio.core.global.enums.Channels;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChannelDetailDto {

    @Schema(description = "상품판매채널")
    private Channels channelEnum;
    @Schema(description = "상품판매채널 이름")
    private String name;
    @Schema(description = "디테일 URL")
    private String detailUrl;



}
