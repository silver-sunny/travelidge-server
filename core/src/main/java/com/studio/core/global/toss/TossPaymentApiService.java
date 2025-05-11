package com.studio.core.global.toss;

import static com.studio.core.global.constant.TossUrlConstant.TOSS_CANCEL_PAYMENT_URL;
import static com.studio.core.global.constant.TossUrlConstant.TOSS_CONFIRM_PAYMENT_URL;
import static com.studio.core.global.constant.TossUrlConstant.TOSS_GET_PAYMENT_URL;
import static com.studio.core.global.constant.naver.NaverProductUrlConstant.NAVER_CANCEL_REQUEST_URL;

import com.studio.core.order.dto.payment.GetPaymentSuccessResponseDto;
import com.studio.core.order.dto.payment.PaymentCancelRequestDto;
import com.studio.core.order.dto.payment.PaymentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TossPaymentApiService {

    private final TossApiService tossApiService;

    public GetPaymentSuccessResponseDto confirmPayment(PaymentRequestDto req) {
        return tossApiService.sendRequest(TOSS_CONFIRM_PAYMENT_URL, HttpMethod.POST, req,
            GetPaymentSuccessResponseDto.class).block();

    }

    public GetPaymentSuccessResponseDto getPayment(String paymentKey) {
        return tossApiService.sendRequest(String.format(TOSS_GET_PAYMENT_URL, paymentKey),
            HttpMethod.GET, null, GetPaymentSuccessResponseDto.class).block();
    }

    public void cancelPayment(String paymentKey, PaymentCancelRequestDto req) {
         tossApiService.sendRequest(String.format(TOSS_CANCEL_PAYMENT_URL, paymentKey),
            HttpMethod.POST, req, Void.class);
    }

}
