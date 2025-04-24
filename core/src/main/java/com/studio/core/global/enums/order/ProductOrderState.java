package com.studio.core.global.enums.order;

import com.studio.core.global.repository.Display;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum ProductOrderState {

    ETC(0, "",false,false),
    ALL(0, "전체",false,false),
    // 자체 사용
    // 자체 사용
    PAYED(10, "결제 완료", true,true),
    DELIVERED(20, "배송 완료",true,false),
    RETURNED(30, "반품", false,false),
    CANCELED(40, "취소", false,false),
    PURCHASE_DECIDED(50, "구매확정", true,true),
    PAYMENT_WAITING(60, "결제대기", false,false),
    CANCELED_BY_NOPAYMENT(70, "미결제취소",false,false),

    // 자체에서 안사용하지만 네이버쪽에 있는 status
    EXCHANGED(80, "교환", false,false),
    DELIVERING(90, "배송 중", false,false);

    private final Integer index;
    private final String meaning;

    private final boolean available;
    private final boolean ticketApplyavailable;



    ProductOrderState(int index, String meaning, boolean available,boolean ticketApplyavailable) {
        this.index = index;
        this.meaning = meaning;
        this.available = available;
        this.ticketApplyavailable = ticketApplyavailable;
    }

    public static ProductOrderState fromIndex(Integer index) {
        return Arrays.stream(ProductOrderState.values())
                .filter(e -> e.index.equals(index))
                .findFirst()
                .orElse(ETC);
    }


    // 검색에 사용할 수 있는 상태만 반환
    public static List<Display> getSearchableStates() {
        return Arrays.stream(ProductOrderState.values())
                .filter(state -> List.of(
                        ALL, PAYMENT_WAITING, CANCELED_BY_NOPAYMENT,
                        PAYED, DELIVERED, RETURNED,
                        CANCELED, PURCHASE_DECIDED
                ).contains(state))
                .map(value -> new Display(value.name(), value.getMeaning()))
                .toList();
    }

    // 검색이 가능한 상태인지 확인
    public boolean isSearchable() {
        return List.of(
                PAYMENT_WAITING, CANCELED_BY_NOPAYMENT,
                PAYED, DELIVERED, RETURNED,
                CANCELED, PURCHASE_DECIDED
        ).contains(this);
    }

    public static ProductOrderState getOrderStateByPayment(PaymentStatus paymentStatus) {
        switch (paymentStatus) {
            case WAITING_FOR_DEPOSIT -> {
                return ProductOrderState.PAYMENT_WAITING;
            }
            case DONE -> {
                return ProductOrderState.PAYED;
            }
        }
//        throw new CustomException(PAYMENT_STATUS_NOT_VALID);
        return ProductOrderState.PAYED;
    }

    public static List<ProductOrderState> productOrderAvailableList() {
        return Arrays.stream(ProductOrderState.values())
            .filter(ProductOrderState::isAvailable)
            .collect(Collectors.toList());
    }

    public static List<ProductOrderState> productOrderTicketApplyAvailableList() {
        return Arrays.stream(ProductOrderState.values())
            .filter(ProductOrderState::isAvailable)
            .collect(Collectors.toList());
    }
}
