package com.studio.api.member.controller;


import static com.studio.api.global.service.CommonService.getMemberFromAuth;

import com.studio.api.global.config.annotaions.CustomExceptionDescription;
import com.studio.api.global.config.annotaions.DisableSwaggerSecurity;
import com.studio.api.global.config.swagger.SwaggerResponseDescription;
import com.studio.api.member.service.RefreshService;
import com.studio.core.member.dto.admin.AdminRegisterRequestDto;
import com.studio.core.member.dto.admin.ChangeAuthDto;
import com.studio.core.member.dto.admin.LoginRequestDto;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.member.repository.MemberQueryRepository;
import com.studio.api.member.service.MemberAuthService;
import com.studio.core.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/v1/api/admin")
@RequiredArgsConstructor
public class AdminController {


    private final MemberAuthService memberAuthService;
private final MemberQueryRepository memberQueryRepository;
    private final RefreshService refreshService;

    @CustomExceptionDescription(SwaggerResponseDescription.PROVIDE_REFRESH_TOKEN)
    @Operation(summary = "리프레시 토큰을 사용하여 엑세스 토큰을 갱신합니다.<br>" +
        "헤더에 accessToken 전달")
    @PostMapping("/refresh")
    public SuccessResponse<Void> refreshToken(HttpServletRequest request,
        HttpServletResponse response) {
        refreshService.refresh(request, response);
        return SuccessResponse.ok();
    }

    @DisableSwaggerSecurity
    @CustomExceptionDescription(SwaggerResponseDescription.ADMIN_LOGIN)
    @Operation(summary = "관리자 계정 로그인", description = "관리자 계정 로그인<br>" +
        "헤더에 <br>" +
        " accessToken -> authorization <br>" +
        "refreshToken-> refresh 으로 이름 되어있음")
    @PostMapping("/login")
    public SuccessResponse<Void> login(@RequestBody LoginRequestDto dto,
        HttpServletResponse response) {
        memberAuthService.login(dto, response);
        return SuccessResponse.ok();
    }

    @CustomExceptionDescription(SwaggerResponseDescription.ADMIN_LOGIN)
    @Operation(summary = "관리자 계정 등록", description = "관리자 계정 등록")
    @PreAuthorize("hasAnyAuthority('ROLE_ROOT')")
    @PostMapping("/register")
    public SuccessResponse<?> registerAdmin(
        @RequestBody AdminRegisterRequestDto adminRegisterRequestDto) {
        memberAuthService.registerAdminAuth(adminRegisterRequestDto);
        return SuccessResponse.ok();
    }

    @Operation(summary = "관리자 아이디 중복 체크", description = "관리자의 아이디가 중복되는지 여부 <br>" +
        "중복되면 true, 중복 아니면 false")
    @GetMapping("/exist-id")
    public SuccessResponse<?> existsById(
        @RequestParam(name = "id") @Size(min = 1, max = 14, message = "아이디는 1~14자여야 합니다.")
        @Pattern(regexp = "^[a-zA-Z0-9]{1,14}$", message = "아이디는 영어 및 숫자로만 구성되어야 합니다.") String id) {

        return SuccessResponse.ok(memberAuthService.existsById(id));
    }

    @Operation(summary = "로그인한 사용자 비밀번호 변경", description = "비밀번호 변경 <br>" +
        "TO-DO 유효성 검사")
    @PatchMapping("/password")
    public SuccessResponse<?> changePassword(@Validated @RequestBody ChangeAuthDto dto,
        Authentication authentication) {

        MemberAuthEntity member = getMemberFromAuth(authentication);

        memberAuthService.updatePassword(dto,member);
        return SuccessResponse.ok();

    }

    @Operation(summary = "비밀번호 초기화", description = "비밀번호 초기화 <br>")
    @PreAuthorize("hasAnyAuthority('ROLE_ROOT')")
    @PatchMapping("/reset-password/{memberNo}")
    public SuccessResponse<?> resetPassword(@PathVariable(name = "memberNo") Long memberNo) {

        memberAuthService.resetPassword(memberNo);
        return SuccessResponse.ok();

    }

    @Operation(summary = "관리자 제거", description = "관리자 제거 <br>")
    @PreAuthorize("hasAnyAuthority('ROLE_ROOT')")
    @DeleteMapping("")
    public SuccessResponse<?> deleteAdmin(@RequestParam(name = "memberNo") Long memberNo) {

        memberAuthService.deleteAdmin(memberNo);

        return SuccessResponse.ok();

    }

    @Operation(summary = "관리자 리스트", description = "관리자 리스트 <br>" +
        "정렬 memberNo , asc  <- memberNo 오름차순<br>" +
        "memberNo , desc <- memberNo로 내림차순 ")
    @PreAuthorize("hasAnyAuthority('ROLE_ROOT')")
    @GetMapping("")
    public SuccessResponse<?> getAdminList(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "memberNo,desc@") String[] sort) {

        Sort.Direction direction =
            sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        return SuccessResponse.ok(memberQueryRepository.findAllExcludingRoleRoot(pageable));

    }
}
