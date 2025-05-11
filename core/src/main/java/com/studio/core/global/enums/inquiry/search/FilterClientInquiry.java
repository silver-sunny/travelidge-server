package com.studio.core.global.enums.inquiry.search;

import com.studio.core.global.repository.Display;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

@Getter
public enum FilterClientInquiry {
    ALL("전체"),
    RESOLVED("답변완료"),
    NOT_RESOLVED("미답변"),
    MY_INQUIRY("내문의");

    private final String meaning;

    FilterClientInquiry(String meaning) {
        this.meaning = meaning;
    }

    public static List<Display> getSearchableStates() {
        return Arrays.stream(FilterClientInquiry.values())
                .map(value -> new Display(value.name(), value.getMeaning()))
                .toList();
    }
}
