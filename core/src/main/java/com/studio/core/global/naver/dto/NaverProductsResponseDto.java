package com.studio.core.global.naver.dto;

import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NaverProductsResponseDto {

    private List<NaverProductContentsDto> contents;

    private int page;

    private int size;

    private Long totalElements;

    private int totalPages;


    // 첫번째 페이지 여부
    private boolean first;

    // 마지막 페이지 여부
    private boolean last;
}
