package com.studio.core.global.utils;

import static com.studio.core.global.exception.ErrorCode.JSON_ERROR;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studio.core.global.exception.CustomException;

public class CommonUil {

    /**
     * 공통 - 객체를 JSON 문자열로 직렬화
     */
    public static String toJson(Object requestDto) {
         ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.writeValueAsString(requestDto);
        } catch (JsonProcessingException e) {
            throw new CustomException(JSON_ERROR, " > JSON 직렬화 실패: " + e.getMessage());
        }
    }

}
