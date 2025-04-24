package com.studio.core.global.enums.order.payment;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
@Getter
public enum PaymentCardCompany {

    NOTHING("","",""),
    BC("3K", "기업비씨", "IBK_BC"),
    GWANGJUBANK("46", "광주", "GWANGJUBANK"),
    LOTTE("71", "롯데", "LOTTE"),
    KDBBANK("30", "산업", "KDBBANK"),
    BC_CARD("31", "-", "BC"),
    SAMSUNG("51", "삼성", "SAMSUNG"),
    SAEMAUL("38", "새마을", "SAEMAUL"),
    SHINHAN("41", "신한", "SHINHAN"),
    SHINHYEOP("62", "신협", "SHINHYEOP"),
    CITI("36", "씨티", "CITI"),
    WOORI_BC("33", "우리", "WOORI"),
    WOORI("W1", "우리", "WOORI"),
    POST("37", "우체국", "POST"),
    SAVINGBANK("39", "저축", "SAVINGBANK"),
    JEONBUKBANK("35", "전북", "JEONBUKBANK"),
    JEJUBANK("42", "제주", "JEJUBANK"),
    KAKAOBANK("15", "카카오뱅크", "KAKAOBANK"),
    KBANK("3A", "케이뱅크", "KBANK"),
    TOSSBANK("24", "토스뱅크", "TOSSBANK"),
    HANA("21", "하나", "HANA"),
    HYUNDAI("61", "현대", "HYUNDAI"),
    KOOKMIN("11", "국민", "KOOKMIN"),
    NONGHYEOP("91", "농협", "NONGHYEOP"),
    SUHYEOP("34", "수협", "SUHYEOP"),
    PAYCO("-", "-", "PCP"),
    KB_SECURITIES("-", "-", "KBS"),
    DINERS("6D", "다이너스", "DINERS"),
    MASTER("4M", "마스터", "MASTER"),
    UNIONPAY("3C", "유니온페이", "UNIONPAY"),
    AMEX("7A", "-", "AMEX"),
    JCB("4J", "-", "JCB"),
    VISA("4V", "비자", "VISA");

    private static final Map<String, PaymentCardCompany> CODE_MAP = new HashMap<>();

    static {
        for (PaymentCardCompany cart : values()) {
            CODE_MAP.put(cart.code, cart);
        }
    }

    private final String code;
    private final String koreanName;
    private final String englishName;

    PaymentCardCompany(String code, String koreanName, String englishName) {
        this.code = code;
        this.koreanName = koreanName;
        this.englishName = englishName;
    }

    @JsonCreator
    public static PaymentCardCompany fromCode(String code) {
        if (code == null || code.isEmpty()) {
            return NOTHING; // Or handle as per your logic
        }
        return CODE_MAP.getOrDefault(code, NOTHING);
    }
}

