package com.studio.core.global.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum FilterProductState {
    ALL(0, "전체"),
    SALE(10, "판매 중"),
    SUSPENSION(20, "판매 대기(중지)"),
    OUTOFSTOCK(30, "품절"),
    PROHIBITION(40, "판매 금지"),
    CLOSE(50, "판매 종료"),
    WAIT(60, "판매 대기"),
    UNADMISSION(70, "승인 대기"),
    REJECTION(80, "승인 거부");

    private final int stateNo;
    private final String meaning;

    FilterProductState(int stateNo, String meaning) {
        this.stateNo = stateNo;
        this.meaning = meaning;
    }


    public static List<Map<String, Object>> getFilterProductStates() {
        return Arrays.stream(FilterProductState.values())
                .map(state -> {
                    Map<String, Object> stateMap = new HashMap<>();
                    stateMap.put("stateNo", state.getStateNo());
                    stateMap.put("meaning", state.getMeaning());
                    stateMap.put("enumValue", state);
                    return stateMap;
                })
                .collect(Collectors.toList());
    }

}

