package com.studio.core.global.naver.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class NaverOrderStatusDataDto {

    private List<NaverLastChangeStatusDto> lastChangeStatuses;

    private int count;


}
