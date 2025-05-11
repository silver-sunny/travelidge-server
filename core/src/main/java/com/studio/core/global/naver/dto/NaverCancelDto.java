package com.studio.core.global.naver.dto;

import com.studio.core.global.enums.order.CancelOrReturnState;
import com.studio.core.global.enums.order.NaverDetailReturnCancleReason;
import lombok.Getter;

@Getter
public class NaverCancelDto {

    // 취소 이유
    private NaverDetailReturnCancleReason cancelReason;

    // 취소 상세 이유
    private String cancelDetailedReason;
    // 취소 요청 날짜
    private String claimRequestDate;

    // 취소 완료날짜
    private String cancelCompletedDate;

    // 취소 주체자
    private String requestChannel;


    private CancelOrReturnState claimStatus;

}
