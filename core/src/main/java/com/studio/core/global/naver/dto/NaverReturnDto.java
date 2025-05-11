package com.studio.core.global.naver.dto;

import com.studio.core.global.enums.order.CancelOrReturnState;
import com.studio.core.global.enums.order.NaverDetailReturnCancleReason;
import lombok.Getter;

@Getter
public class NaverReturnDto {

    // 상태
    private CancelOrReturnState claimStatus;

    // 반품 요청 날짜
    private String claimRequestDate;

    // 반품 이유
    private NaverDetailReturnCancleReason returnReason;

    // 반품 상세이유
    private String returnDetailedReason;

    // 반품 완효 날짜
    private String returnCompletedDate;

    // 반품 주체자
    private String requestChannel;
}
