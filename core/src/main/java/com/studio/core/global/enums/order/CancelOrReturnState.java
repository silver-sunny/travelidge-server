package com.studio.core.global.enums.order;

import com.studio.core.global.repository.Display;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum CancelOrReturnState {
    NOTHING(0, "-", true),
    ALL(0, "전체", false),

    // 자체 액션 가능
    CANCEL_REQUEST(10, "취소 요청", false),
    CANCEL_REJECT(11, "취소 철회", true),
    CANCEL_DONE(12, "취소 처리 완료", false),
    CANCELING(13, "취소 처리 중", false),

    RETURN_REQUEST(20, "반품 요청", false),
    RETURN_REJECT(21, "반품 철회", true),
    RETURN_DONE(22, "반품 완료", false),

    PURCHASE_DECISION_REQUEST(30, "확정 요청", true),
    PURCHASE_DECISION_HOLDBACK_RELEASE(31, "확정보류해제", true),
    PURCHASE_DECISION_HOLDBACK(32, "확정보류", true),

    ADMIN_CANCEL_REJECT(40, "직권취소철회", true),

    // 자체액션 안됨
    ADMIN_CANCEL_DONE(41, "직권취소완료", false),
    ADMIN_CANCELING(42, "직권취소중", false),

    COLLECTING(50, "수거처리중", false),
    COLLECT_DONE(51, "수거완료", false),

    EXCHANGE_REQUEST(60, "교환요청", false),
    EXCHANGE_REJECT(61, "교환철회", false),
    EXCHANGE_DONE(62, "교환완료", false),
    EXCHANGE_REDELIVERING(63, "교환재배송중", false);


    private final Integer index;
    private final String meaning;
    private final boolean usable;

    CancelOrReturnState(Integer index, String meaning, boolean usable) {
        this.index = index;
        this.meaning = meaning;
        this.usable = usable;
    }


    public static CancelOrReturnState fromIndex(Integer index) {
        return Arrays.stream(CancelOrReturnState.values())
                .filter(e -> e.index.equals(index))
                .findFirst()
                .orElse(NOTHING);
    }

    // 검색에 사용할 수 있는 상태만 반환
    public static List<Display> getSearchableStates() {
        return Arrays.stream(CancelOrReturnState.values())
                .filter(state -> List.of(
                        ALL, CANCEL_REQUEST, CANCEL_DONE,
                        CANCEL_REJECT, RETURN_REQUEST, RETURN_DONE,
                        RETURN_REJECT
                ).contains(state))
                .map(value -> new Display(value.name(), value.getMeaning()))
                .toList();
    }

    // 검색이 가능한 상태인지 확인
    public boolean isSearchable() {
        return List.of(
                CANCEL_REQUEST, CANCEL_DONE,
                CANCEL_REJECT, RETURN_REQUEST, RETURN_DONE,
                RETURN_REJECT
        ).contains(this);
    }
    public static List<CancelOrReturnState> cancelOrReturnStateTicketIssuedAvailableList() {
        return Arrays.stream(CancelOrReturnState.values())
            .filter(CancelOrReturnState::isUsable)
            .collect(Collectors.toList());
    }
}
