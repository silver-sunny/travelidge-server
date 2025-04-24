package com.studio.core.inquiry.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.studio.core.global.enums.inquiry.InquiryPrivateState;
import com.studio.core.global.enums.inquiry.InquiryResolvedState;
import com.studio.core.global.utils.TimeCalculatorUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "상품 문의내역 리스트")
public record GetClientProductInquiryTableResponse(

    @Schema(description = "문의 번호")
    Long id,

    @Schema(description = "문의")
    String inquiry,

    @Schema(description = "답변")
    String answer,

    @Schema(description = "문의 날짜")
    LocalDateTime inquiryAt,

    @Schema(description = "답변 날짜")
    LocalDateTime answerAt,

    @Schema(description = "상품번호")
    Long productId,

    @Schema(description = "비밀문의 여부")
    InquiryPrivateState isPrivate,

    @Schema(description = "해결여부")
    InquiryResolvedState isResolved,

    @Schema(description = "문의 작성자 회원번호")
    Long memberNo,

    @Schema(description = "문의 작성자 닉네임")
    String nickname


) {

    @QueryProjection
    public GetClientProductInquiryTableResponse {}

    @Schema(description = "문의 날짜")
    public String getInquiryAt() {
        return TimeCalculatorUtil.getFormattedDate(inquiryAt);
    }

    @Schema(description = "답변 날짜")
    public String getAnswerAt() {
        return TimeCalculatorUtil.getFormattedDate(answerAt);
    }

    @Schema(description = "답변여부")
    public String getResolvedMeaning() {
        return isResolved.getMeaning();
    }

    @Schema(description = "공개여부")
    public String getIsPrivateMeaning() {
        return isPrivate.getMeaning();
    }

    @Schema(description = "판매자명")
    public String getSellerName() {
        return "판매자";
    }
}

