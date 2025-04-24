package com.studio.core.global.enums.order.payment;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public enum PaymentFinancialInstitution {

    // 은행
    KYONGNAMBANK("039", "39", "경남", "KYONGNAMBANK"),
    GWANGJUBANK("034", "34", "광주", "GWANGJUBANK"),
    LOCALNONGHYEOP("012", "12", "단위농협", "LOCALNONGHYEOP"),
    BUSANBANK("032", "32", "부산", "BUSANBANK"),
    SAEMAUL("045", "45", "새마을", "SAEMAUL"),
    SANLIM("064", "64", "산림", "SANLIM"),
    SHINHAN("088", "88", "신한", "SHINHAN"),
    SHINHYEOP("048", "48", "신협", "SHINHYEOP"),
    CITI("027", "27", "씨티", "CITI"),
    WOORI("020", "20", "우리", "WOORI"),
    POST("071", "71", "우체국", "POST"),
    SAVINGBANK("050", "50", "저축", "SAVINGBANK"),
    JEONBUKBANK("037", "37", "전북", "JEONBUKBANK"),
    JEJUBANK("035", "35", "제주", "JEJUBANK"),
    KAKAOBANK("090", "90", "카카오", "KAKAOBANK"),
    KBANK("089", "89", "케이", "KBANK"),
    TOSSBANK("092", "92", "토스", "TOSSBANK"),
    HANA("081", "81", "하나", "HANA"),
    HSBC("054", "54", "-", "HSBC"),
    IBK("003", "03", "기업", "IBK"),
    KOOKMIN("004", "06", "국민", "KOOKMIN"),
    DAEGUBANK("031", "31", "대구", "DAEGUBANK"),
    KDBBANK("002", "02", "산업", "KDBBANK"),
    NONGHYEOP("011", "11", "농협", "NONGHYEOP"),
    SC("023", "23", "SC제일", "SC"),
    SUHYEOP("007", "07", "수협", "SUHYEOP"),

    // 증권
    KYOBO_SECURITIES("261", "S8", "교보증권", "KYOBO_SECURITIES"),
    DAISHIN_SECURITIES("267", "SE", "대신증권", "DAISHIN_SECURITIES"),
    MERITZ_SECURITIES("287", "SK", "메리츠증권", "MERITZ_SECURITIES"),
    MIRAE_ASSET_SECURITIES("238", "S5", "미래에셋증권", "MIRAE_ASSET_SECURITIES"),
    BOOKOOK_SECURITIES("290", "SM", "부국", "BOOKOOK_SECURITIES"),
    SAMSUNG_SECURITIES("240", "S3", "삼성증권", "SAMSUNG_SECURITIES"),
    SHINYOUNG_SECURITIES("291", "SN", "신영증권", "SHINYOUNG_SECURITIES"),
    SHINHAN_SECURITIES("278", "S2", "신한금융투자", "SHINHAN_SECURITIES"),
    YUANTA_SECURITIES("209", "S0", "유안타증권", "YUANTA_SECURITES"),
    EUGENE_INVESTMENT_AND_SECURITIES("280", "SJ", "유진투자증권", "EUGENE_INVESTMENT_AND_SECURITIES"),
    KAKAOPAY_SECURITIES("288", "SQ", "카카오페이증권", "KAKAOPAY_SECURITIES"),
    KIWOOM("264", "SB", "키움증권", "KIWOOM"),
    TOSS_MONEY("-", "-", "토스머니", "TOSS_MONEY"),
    TOSS_SECURITIES("271", "ST", "토스증권", "TOSS_SECURITIES"),
    KOREA_FOSS_SECURITIES("294", "SR", "펀드온라인코리아", "KOREA_FOSS_SECURITIES"),
    HANA_INVESTMENT_AND_SECURITIES("270", "SH", "하나금융투자", "HANA_INVESTMENT_AND_SECURITIES"),
    HI_INVESTMENT_AND_SECURITIES("262", "S9", "하이투자증권", "HI_INVESTMENT_AND_SECURITIES"),
    KOREA_INVESTMENT_AND_SECURITIES("243", "S6", "한국투자증권", "KOREA_INVESTMENT_AND_SECURITIES"),
    HANHWA_INVESTMENT_AND_SECURITIES("269", "SG", "한화투자증권", "HANHWA_INVESTMENT_AND_SECURITIES"),
    HYUNDAI_MOTOR_SECURITIES("263", "SA", "현대차증권", "HYUNDAI_MOTOR_SECURITIES"),
    DB_INVESTMENT_AND_SECURITIES("279", "SI", "DB금융투자", "DB_INVESTMENT_AND_SECURITIES"),
    KB_SECURITIES("218", "S4", "KB증권", "KB_SECURITIES"),
    DAOL_INVESTMENT_AND_SECURITIES("227", "SP", "KTB투자증권", "DAOL_INVESTMENT_AND_SECURITIES"),
    LIG_INVESTMENT_AND_SECURITIES("292", "SO", "LIG투자", "LIG_INVESTMENT_AND_SECURITIES"),
    NH_INVESTMENT_AND_SECURITIES("247", "SL", "NH투자증권", "NH_INVESTMENT_AND_SECURITIES"),
    SK_SECURITIES("266", "SD", "SK증권", "SK_SECURITIES");

    private static final Map<String, PaymentFinancialInstitution> CODE_MAP = new HashMap<>();

    static {
        for (PaymentFinancialInstitution institution : values()) {
            CODE_MAP.put(institution.code, institution);
        }
    }

    private final String officialCode;
    private final String code;
    private final String koreanName;
    private final String englishName;

    PaymentFinancialInstitution(String officialCode, String code, String koreanName,
        String englishName) {
        this.officialCode = officialCode;
        this.code = code;
        this.koreanName = koreanName;
        this.englishName = englishName;
    }


    public static PaymentFinancialInstitution fromCode(String code) {
        return CODE_MAP.get(code);
    }
}
