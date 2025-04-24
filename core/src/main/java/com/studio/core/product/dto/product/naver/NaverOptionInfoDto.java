package com.studio.core.product.dto.product.naver;

import java.util.Collections;
import java.util.List;
import lombok.Getter;

@Getter
public class NaverOptionInfoDto {

    private List<NaverOptionCustomDto> optionCustom;

    public NaverOptionInfoDto() {
        this.optionCustom = Collections.singletonList(new NaverOptionCustomDto());
    }
}
