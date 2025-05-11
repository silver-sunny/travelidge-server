package com.studio.api.product.controller;

import static com.studio.api.global.service.CommonService.getMemberFromAuth;

import com.studio.api.product.service.PaymentService;
import com.studio.core.global.response.SuccessResponse;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.order.dto.payment.PaymentRequestDto;
import com.studio.core.order.dto.payment.SaveAmountRequestDto;
import com.studio.core.order.dto.payment.TemPaymentDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/client/payment")
@Tag(name = "client-payment-controller", description = "클라 결제 관리 API")
@RequiredArgsConstructor
public class PaymentController {


    private final PaymentService paymentService;

    /**
     * 결제의 금액을  임시저장 결제 과정에서 악의적으로 결제 금액이 바뀌는 것을 확인하는 용도
     */
    @Operation(summary = "결제 요청하기전 임시저장, orderId 전달")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SaveAmountRequestDto.class)))
    })
    @PostMapping("/saveAmount")
    public SuccessResponse<TemPaymentDto> tempsave(
        @RequestBody SaveAmountRequestDto saveAmountRequest,
        Authentication authentication
        ) {

        MemberAuthEntity member = getMemberFromAuth(authentication);

        return SuccessResponse.ok(paymentService.temPayment(saveAmountRequest,member));

    }


    @PostMapping(value = "/confirm")
    public SuccessResponse<?> confirmPayment(@RequestBody PaymentRequestDto requestDto,
        Authentication authentication) {
        MemberAuthEntity member = getMemberFromAuth(authentication);

        paymentService.confirmPayment(requestDto,member);
        return SuccessResponse.ok();

    }


    @GetMapping(value = "/payment/{paymentKey}")
    public SuccessResponse<?> getPayment(@PathVariable String paymentKey) {

        return SuccessResponse.ok(paymentService.getPayment(paymentKey));

    }

}
