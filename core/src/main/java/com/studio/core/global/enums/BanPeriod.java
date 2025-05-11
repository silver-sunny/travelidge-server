package com.studio.core.global.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum BanPeriod {
    WEEK(7, "일주일"),
    MONTH(30, "한달"),
    PERMANENT(-1, "영구차단"),
    NONE(0, "반려");


    private final int days;
    private final String meaning;
    BanPeriod(int days, String meaning) {
        this.days = days;
        this.meaning = meaning;

    }

    public int getDays() {
        return days;
    }

    public boolean isPermanent() {
        return this == PERMANENT;
    }

    public boolean isUnban() {
        return this == NONE;
    }



    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static BanPeriod from(String value) {
        try {
            return BanPeriod.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("지원하지 않는 차단 기간입니다: " + value);
        }
    }


    public static List<Map<String, Object>> getBanPeriod() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (BanPeriod value : BanPeriod.values()) {

            if (value == BanPeriod.NONE) {
                continue;
            }

            Map<String, Object> banMap = new HashMap<>();

            banMap.put("meaning", value.meaning);
            banMap.put("enumValue", value);

            list.add(banMap);
        }
        return list;
    }

    @JsonValue
    public String toJson() {
        return name();
    }
}

