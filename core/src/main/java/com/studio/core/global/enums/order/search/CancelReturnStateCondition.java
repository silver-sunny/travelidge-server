package com.studio.core.global.enums.order.search;

import com.studio.core.global.repository.Display;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

@Getter
public enum CancelReturnStateCondition {
    ALL("전체"),
    CANCEL("취소"),
    RETURN("반품");

    private final String meaning;

    CancelReturnStateCondition(String meaning) {
        this.meaning = meaning;
    }

    public static List<Display> getSearchableStates() {
        return Arrays.stream(CancelReturnStateCondition.values())
                .map(value -> new Display(value.name(), value.getMeaning()))
                .toList();
    }


}

