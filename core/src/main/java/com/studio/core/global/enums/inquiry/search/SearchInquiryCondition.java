package com.studio.core.global.enums.inquiry.search;

import com.studio.core.global.repository.Display;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

@Getter
public enum SearchInquiryCondition {
    PRODUCT_NAME("상품명"),
    PRODUCT_ID("상품번호");

    private final String meaning;

    SearchInquiryCondition(String meaning) {
        this.meaning = meaning;
    }

    public static List<Display> getSearchableStates() {
        return Arrays.stream(SearchInquiryCondition.values())
                .map(value -> new Display(value.name(), value.getMeaning()))
                .toList();
    }


}

