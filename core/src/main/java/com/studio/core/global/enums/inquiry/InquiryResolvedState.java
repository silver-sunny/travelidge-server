package com.studio.core.global.enums.inquiry;

import lombok.Getter;

@Getter
public enum InquiryResolvedState {
    NOT_RESOLVED("미답변", false),
    RESOLVED("답변완료", true);

    private final String meaning;
    private final Boolean isResolved;

    InquiryResolvedState(String meaning, Boolean isResolved) {
        this.meaning = meaning;
        this.isResolved = isResolved;
    }
}
