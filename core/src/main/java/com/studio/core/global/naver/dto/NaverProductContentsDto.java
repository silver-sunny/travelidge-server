package com.studio.core.global.naver.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class NaverProductContentsDto {

    private Long originProductNo;

    private List<NaverChannelProductsDto> channelProducts;


}
