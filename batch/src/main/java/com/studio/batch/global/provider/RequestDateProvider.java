package com.studio.batch.global.provider;


import com.studio.core.global.utils.DateUtil;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RequestDateProvider {

    @Value("${requestDate:#{null}}")
    private String requestDate;

    private String formattedRequestDate;

    @PostConstruct
    public void init() {
        // requestDate가 설정된 경우에만 처리
        if (requestDate != null) {
            this.formattedRequestDate = getRequestDateTime(requestDate);
        }
    }

    public static String getRequestDateTime(String requestDate) {
        String DEFAULT_TIME = "T00:00:00+09:00";

        if (requestDate != null) {
            // program arg에 있으면 date로 파싱 (확인 후 format return)
            String dateString = requestDate + DEFAULT_TIME;

            // OffsetDateTime으로 파싱
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateString);
            return DateUtil.dateToFormatStringAndEncode(offsetDateTime);
        }

        // 현재 날짜를 가져오고 DEFAULT_TIME을 붙여 OffsetDateTime 생성
        String nowDateString = LocalDate.now() + DEFAULT_TIME;
        OffsetDateTime nowOffsetDateTime = OffsetDateTime.parse(nowDateString);

        return DateUtil.dateToFormatStringAndEncode(nowOffsetDateTime);
    }

    public String getFormattedRequestDateTime() {
        // requestDate가 주입되지 않았다면 현재 날짜를 반환
        return formattedRequestDate != null ? formattedRequestDate : getRequestDateTime(null);
    }

}