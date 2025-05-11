package com.studio.api.member.controller;

import static com.studio.api.global.service.CommonService.getMemberFromAuth;

import com.studio.api.member.service.MemberAuthService;
import com.studio.api.order.service.OrderService;
import com.studio.api.review.service.ReviewService;
import com.studio.core.global.response.SuccessResponse;
import com.studio.core.member.dto.admin.MyUserInfoResponseDto;
import com.studio.core.member.dto.admin.UpdateUserRequestDto;
import com.studio.core.member.dto.user.GetPurchaseAndReviewCountResponse;
import com.studio.core.member.entity.MemberAuthEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/v1/api/my")
@Tag(name = "my-controller", description = "마이 유저 API")
@RequiredArgsConstructor
public class MyController {

    private final MemberAuthService memberAuthService;

    private final ReviewService reviewService;

    private final OrderService orderService;


    @Operation(summary = "이름, 닉네임, 전화번호 변경", description = "이름, 닉네임, 전화번호 변경  <br>"
        + "name -> 2~10글자 사이 <br>"
        + "nickname -> 2~14글자 사이"
        + "이중에 값이 있으면 변경")
    @PostMapping
    public SuccessResponse<?> updateUser(@Validated @RequestBody UpdateUserRequestDto dto,
        Authentication authentication) {

        MemberAuthEntity member = getMemberFromAuth(authentication);

        memberAuthService.updateUser(dto, member);

        return SuccessResponse.ok();

    }


    @Operation(summary = "닉네임 중복 체크", description = "닉네임 중복되는지 여부 <br>"
        + "nickname -> 2~14글자 사이" +
        "중복되면 true, 중복 아니면 false")
    @GetMapping("/exist-nickname")
    public SuccessResponse<?> existsById(
        @RequestParam(name = "nickname") @Size(min = 2, max = 14) String nickname) {

        return SuccessResponse.ok(memberAuthService.existsByNickname(nickname));
    }

    @Operation(summary = "나의 정보")
    @GetMapping("")
    public SuccessResponse<?> myInfo(Authentication authentication) {
        MemberAuthEntity member = getMemberFromAuth(authentication);

        return SuccessResponse.ok(new MyUserInfoResponseDto(member));
    }


    @Operation(summary = "나의 구매갯수, 작성가능한 후기 갯수")
    @GetMapping("/purchase-review-count")
    public SuccessResponse<?> myPurchaseReviewCount(Authentication authentication) {
        MemberAuthEntity member = getMemberFromAuth(authentication);

        Long reviewCount = reviewService.countUnReviewByMember(member);
        Long purchaseCount = orderService.countPurchaseByMember(member);

        return SuccessResponse.ok(
            new GetPurchaseAndReviewCountResponse(purchaseCount, reviewCount));
    }

}
