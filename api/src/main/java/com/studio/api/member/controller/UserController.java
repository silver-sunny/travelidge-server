package com.studio.api.member.controller;

import com.studio.core.global.response.SuccessResponse;
import com.studio.core.member.repository.MemberQueryRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/v1/api/user")
@RequiredArgsConstructor
public class UserController {

    private final MemberQueryRepository memberQueryRepository;


    @Operation(summary = "유저 리스트" ,description = "유저 리스트 <br>" +
            "정렬 memberNo , asc  <- memberNo 오름차순<br>" +
            "memberNo , desc <- memberNo로 내림차순 ")
    @PreAuthorize("hasAnyAuthority('ROLE_ROOT','ROLE_MANAGE','ROLE_GUEST')")
    @GetMapping("")
    public SuccessResponse<?> getUserList(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size,
                                                                    @RequestParam(defaultValue = "memberNo,desc@") String[] sort) {

        Sort.Direction direction =
                sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));


        return SuccessResponse.ok(memberQueryRepository.getUsers(pageable));

    }
}
