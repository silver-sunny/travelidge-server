package com.studio.core.global.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.studio.core.global.enums.product.naver.NaverProductStatusTypeEnum;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum ProductState {

    ETC(0, ""),
    SALE(10, "판매 중"),
    SUSPENSION(20, "판매 대기(중지)"),
    OUTOFSTOCK(30, "품절"),
    PROHIBITION(40, "판매 금지"),
    CLOSE(50, "판매 종료"),
    WAIT(60, "판매 대기"),
    UNADMISSION(70, "승인 대기"),
    REJECTION(80, "승인 거부"),
    DELETE(90, "삭제");

    private final Integer stateNo;
    private final String meaning;

    ProductState(Integer stateNo, String meaning) {
        this.stateNo = stateNo;
        this.meaning = meaning;
    }

    // 역직렬화 메서드 개선
    @JsonCreator
    public static ProductState from(String inputValue) {
        if (inputValue == null || ETC.name().equalsIgnoreCase(inputValue)) {
            throw new IllegalArgumentException("유효하지 않은 상태: " + inputValue);
        }
        return Stream.of(ProductState.values())
                .filter(state -> state.name().equalsIgnoreCase(inputValue))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상태: " + inputValue));
    }

    // 특정 상태만 반환하는 유틸리티
    public static List<Map<String, Object>> getInsertOrUpdateStates() {
        return Stream.of(SALE, SUSPENSION)
                .map(state -> {
                    Map<String, Object> stateMap = new HashMap<>();
                    stateMap.put("stateNo", state.stateNo);
                    stateMap.put("meaning", state.meaning);
                    stateMap.put("enumValue", state);
                    return stateMap;
                })
                .collect(Collectors.toList());
    }


    public static List<Map<String, Object>> getFilterProductStates() {
        return Arrays.stream(ProductState.values())
                .filter(state -> state != ETC && state != DELETE) // Exclude ETC and DELETE
                .map(state -> {
                    Map<String, Object> stateMap = new HashMap<>();
                    stateMap.put("stateNo", state.getStateNo());
                    stateMap.put("meaning", state.getMeaning());
                    stateMap.put("enumValue", state);
                    return stateMap;
                })
                .collect(Collectors.toList());
    }


    // stateNo로 Enum 반환
    public static ProductState fromStateNo(Integer stateNo) {
        return Stream.of(ProductState.values())
                .filter(state -> Objects.equals(state.stateNo, stateNo))
                .findFirst()
                .orElse(ETC);
    }


    // 수정 가능한 상태인지 확인
    public static boolean isModifiable(ProductState state) {
        return EnumSet.of(SALE, OUTOFSTOCK, UNADMISSION, SUSPENSION).contains(state);
    }

    public static ProductState getProductStateByNaverProductStatusEnum(
        NaverProductStatusTypeEnum naverProductStatusTypeEnum) {

        for (ProductState value : ProductState.values()) {
            if(naverProductStatusTypeEnum.name().equals(value.name())){
                return value;
            }
        }

        return ETC;
    }
}

