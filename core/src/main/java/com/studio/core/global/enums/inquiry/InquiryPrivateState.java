package com.studio.core.global.enums.inquiry;

import com.studio.core.global.exception.CustomException;
import com.studio.core.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum InquiryPrivateState {

    PUBLIC("공개", false),
    PRIVATE("비공개", true);

    private final String meaning;
    private final Boolean isPrivate;

    InquiryPrivateState(String meaning, Boolean isPrivate) {
        this.meaning = meaning;
        this.isPrivate = isPrivate;
    }

    public static InquiryPrivateState fromBoolean(Boolean isPrivate) {
        for (InquiryPrivateState state : InquiryPrivateState.values()) {
            if (state.isPrivate.equals(isPrivate)) {
                return state;
            }
        }
        throw new CustomException(ErrorCode.NOT_AVAILABLE);
    }
}
