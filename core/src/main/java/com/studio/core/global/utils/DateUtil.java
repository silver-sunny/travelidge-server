package com.studio.core.global.utils;

import io.micrometer.common.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    /**
     * 2024-01-12T10:17:22.0+09:00 형태의 값
     * localDateTime으로 변경
     *
     * @param date
     * @return
     */

    public static LocalDateTime stringDateTimeToLocalDateTimeWithFormat(String date) {

        if (StringUtils.isEmpty(date)) {
            return null;
        }

        return LocalDateTime.parse(date.replaceAll("\\.\\d+\\+\\d+:\\d+", ""), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }

    public static String dateToFormatStringAndEncode(OffsetDateTime searchDate) {

        if (searchDate == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        return searchDate.format(formatter);


    }

    public static String getCurrentDateAsString() {
        // 현재 날짜 가져오기
        LocalDate currentDate = LocalDate.now();

        // 날짜 형식 지정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 형식에 맞게 출력
        return currentDate.format(formatter);

    }
}
