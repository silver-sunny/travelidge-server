package com.studio.core.global.constant.naver;

public class NaverProductUrlConstant {

    // 반품 승인
    public static final String NAVER_RETURN_APPROVE_URL = "https://api.commerce.naver.com/external/v1/pay-order/seller/product-orders/%s/claim/return/approve";

    // 반품 거부
    public static final String NAVER_RETURN_REJECT_URL = "https://api.commerce.naver.com/external/v1/pay-order/seller/product-orders/%s/claim/return/reject";

    // 반품 요청
    public static final String NAVER_RETURN_REQUEST_URL = "https://api.commerce.naver.com/external/v1/pay-order/seller/product-orders/%s/claim/return/request";

    // 취소 승인
    public static final String NAVER_CANCEL_APPROVE_URL = "https://api.commerce.naver.com/external/v1/pay-order/seller/product-orders/%s/claim/cancel/approve";

    // 취소 요청
    public static final String NAVER_CANCEL_REQUEST_URL = "https://api.commerce.naver.com/external/v1/pay-order/seller/product-orders/%s/claim/cancel/request";

    // 배송
    public static final String NAVER_DISPATCH_URL = "https://api.commerce.naver.com/external/v1/pay-order/seller/product-orders/dispatch";

    // 상품 추가
    public static final String NAVER_INSERT_PRODUCT_URL = "https://api.commerce.naver.com/external/v2/products";

    // 이미지 증록
    public static final String NAVER_INSERT_PRODUCT_IMAGES_URL = "https://api.commerce.naver.com/external/v1/product-images/upload";

    // 상품 정보 출력
    public static final String NAVER_GET_PRODUCT_URL = "https://api.commerce.naver.com/external/v2/products/channel-products/";

    // 상품 수정
    public static final String NAVER_UPDATE_PRODUCT_URL = "https://api.commerce.naver.com/external/v2/products/channel-products/";

    // 상품 삭제
    public static final String NAVER_DELETE_PRODUCT_URL = "https://api.commerce.naver.com/external/v2/products/channel-products/";

    // 상품 주문 channelOrderId로 가져오기
    public final static String GET_NAVER_PRODUCT_ORDERS_URL = "https://api.commerce.naver.com/external/v1/pay-order/seller/product-orders/query";

    public final static String GET_NAVER_PRODUCT_ORDER_URL = "https://api.commerce.naver.com/external/v1/pay-order/seller/product-orders/last-changed-statuses";

    public final static String GET_NAVER_PRODUCTS_SEARCH_URL = "https://api.commerce.naver.com/external/v1/products/search";

}
