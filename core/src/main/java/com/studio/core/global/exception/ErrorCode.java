package com.studio.core.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum ErrorCode {

    /**
     * 에러코드 규약
     * HTTP Status Code는 에러에 가장 유사한 코드를 부여한다.(Swagger에서 구분하여 Response Example로 출력된다.)
     * 사용자정의 에러코드는 음수를 사용한다.
     * 사용자정의 에러코드는 중복되지 않게 배정한다.
     * 사용자정의 에러코드는 각 카테고리 별로 100단위씩 끊어서 배정한다. 단, Common 카테고리는 -100 단위를 고정으로 가져간다.
     */


    /**
     * 401 : 미승인 403 : 권한의 문제가 있을때 406 : 객체가 조회되지 않을 때 409 : 현재 데이터와 값이 충돌날 때(ex. 아이디 중복) 412 : 파라미터
     * 값이 뭔가 누락됐거나 잘못 왔을 때 422 : 파라미터 문법 오류 424 : 뭔가 단계가 꼬였을때, 1번안하고 2번하고 그런경우..승인 안거친 그런경우 425 : 기능
     * 조건에 맞지 않을경우
     */

    //Common
    SERVER_UNTRACKED_ERROR(-100, "미등록 서버 에러입니다. 서버 팀에 연락주세요.", 500),
    OBJECT_NOT_FOUND(-101, "조회된 객체가 없습니다.", 406),
    INVALID_PARAMETER(-102, "잘못된 파라미터입니다. : ", 422),
    PARAMETER_VALIDATION_ERROR(-103, "파라미터 검증 에러입니다.", 422),
    PARAMETER_GRAMMAR_ERROR(-104, "파라미터 문법 에러입니다.", 422),
    INVALID_ORDER_PARAMETER(-105, "잘못된 정렬 조건 파라미터입니다.", 422),
    INVALID_TYPE_PARAMETER(-106, "잘못된 타입 파라미터입니다. : ", 422),
    COUNT_MAX_ERROR(-107, "최대 갯수를 초과했습니다", 412),
    COUNT_MIN_ERROR(-108, "최소 갯수 미만입니다.", 412),
    NO_REQUIRED_VALUE(-109, "필수값 없음 : ", 412),
    JSON_ERROR(-110, "json 파싱 에러", 500),
    NOT_AVAILABLE(-111, "해당하는 값이 없습니다.", 406),
    ORACLE_BUCKET_FAIL(-112, "OCI 서버 통신 중 오류가 발생했습니다.", 424),
    ORACLE_BUCKET_NOT_EXIST(-113, "대상 파일이 존재하지 않습니다.", 406),
    FILE_UPLOAD_COUNT_EXCEED(-113, "업로드 가능한 파일 수를 초과했습니다.", 412),
    //Auth
    UNAUTHORIZED(-200, "인증 자격이 없습니다.", 401),
    FORBIDDEN(-201, "권한이 없습니다.", 403),
    JWT_ERROR_TOKEN(-202, "잘못된 토큰입니다.", 401),
    JWT_EXPIRE_TOKEN(-203, "만료된 토큰입니다.", 401),
    AUTHORIZED_ERROR(-204, "인증 과정 중 에러가 발생했습니다.", 500),
    JWT_UNMATCHED_CLAIMS(-206, "토큰 인증 정보가 일치하지 않습니다", 401),
    JWT_NOT_EXPIRE_TOKEN(-207, "만료된 토큰이 아닙니다.", 401),
    NOT_VALID_MEMBER(-208, "해당 요청을 수행할 권한이 없습니다(본인께 아님)", 425),
    BANNED_USER(-209, "차단된 회원입니다.", 403),

    //Util
    FILE_CREATE_ERROR(-300, "파일 생성 에러입니다..", 500),
    NAVER_REST_SEND_ERROR(-310, "네이버 rest api로 요청 발송 중 에러가 발생했습니다.", 500),
    TOSS_REST_SEND_ERROR(-311, "토스 rest api로 요청 발송 중 에러가 발생했습니다.", 500),

    // MEMBER
    ADMIN_ALREADY_ID(-600, "아이디 중복입니다.", 409),
    MEMBER_NOT_FOUND(-601, "멤버가 없습니다.", 406),
    ADMIN_PASSWORD_MISMATCH(-602, "비밀번호가 일치하지 않습니다.", 401),
    NOT_ADMIN_OR_AVAILABLE(-603, "관리자 혹은 가능한 관리자가 아닙니다.", 425),
    ALREADY_NICKNAME(-604, "닉네임 중복입니다.", 409),
    MEMBER_SIGN_FAIL(-605, "회원가입에 실패했습니다.", 401),

    // CHANNEL
    NOT_EXPECT_CHANNEL(-700, "가능한 채널이 아닙니다.", 425),

    // PRODUCT
    PRODUCT_NOT_FOUND(-800, "상품이 없습니다.", 406),
    PRODUCT_NOT_VALID_STATE(-801, "요청할수 없는 상태입니다.", 425),
    RECOMMENDED_LIMIT_EXCEEDED(-802, "추천 상품 등록은 최대 10개까지 가능합니다.", 425),
    ALREADY_RECOMMENDED(-803, "이미 등록된 추천 상품입니다.", 409),
    STOCK_OVER(-804, "재고를 넘었습니다.", 425),
    PRODUCT_NOT_SALE(-805, "판매중인 상품이 아닙니다.", 425),
    CART_NOT_FOUND(-806, "해당하는 장바구니가 없습니다.", 406),
    PRODUCT_NOT_VALID_ORDER_STATE(-807, "판매중이 아닌 상품입니다.", 425),


    // ORDER
    ORDER_NOT_FOUND(-900, "주문내역이 없습니다.", 406),
    IS_PROGRESSING(-901, "처리중입니다.", 409),
    NOT_VALID_STATE(-902, "요청할수 없는 상태입니다.", 425),
    ORDER_CART_NOT_AVAILABLE(-903, "주문할 상품이 존재하지 않습니다.", 406),
    PAYMENT_REQUEST_NOT_FOUND(-903, "결제 요청된 건이 없습니다.", 406),
    PAYMENT_AMOUNT_NOT_MATCH(-904, "amount 금액이 일치하지 않습니다.", 425),
    PAYMENT_STATUS_NOT_VALID(-905, "주문가능한 결제상태가 아닙니다.", 425),
    PAYMENT_CONFIRM_STATUS_NOT_VALID(-906, "결제 요청이 아닌 상태입니다.", 425),
    ALREADY_ORDER(-907, "이미 주문한 상품입니다.", 425),
    // TICKET
    TICKET_NOT_FOUND(-1000, "티켓내역이 없습니다.", 406),
    TICKET_NOT_AVAILABLE(-1001, "사용할수 없는 티켓입니다.", 425),
    TICKET_USED(-1002, "사용한 티켓이 있습니다.", 425),
    TICKET_NOT_USED(-1003, "사용한 티켓이 없습니다.", 425),


    //INQUIRY
    INQUIRY_NOT_FOUND(-1100, "문의내역이 없습니다.", 406),

    // REVIEW
    REVIEW_EXIST(-1200, "이미 리뷰를 작성했습니다.", 409),
    REVIEW_NOT_FOUND(-1201, "작성한 리뷰가 없습니다.", 409);
    private final int errorCode;
    private final String message;
    private final int httpCode;

}
