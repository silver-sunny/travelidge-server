package com.studio.api.file.controller;


import static com.studio.api.global.service.CommonService.getMemberFromAuth;

import com.studio.api.image.OracleStorageService;
import com.studio.core.global.naver.file.FileService;
import com.studio.core.global.response.SuccessResponse;
import com.studio.core.member.entity.MemberAuthEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/api/client/images")
@Tag(name = "file-controller", description = "FILE API")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    private final OracleStorageService storageService;


    @Operation(summary = "이미지 등록", description = "")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SuccessResponse<String> uploadImage(
        @RequestPart("file") MultipartFile file,
        Authentication authentication
        ) throws Exception {

        MemberAuthEntity member = getMemberFromAuth(authentication);

        return SuccessResponse.ok(storageService.upload(file,member));
    }


    @Operation(summary = "이미지 삭제", description = "fileName은 / 제일 뒤에있는 값만 사용")
    @DeleteMapping
    public SuccessResponse<Void> deleteImage(@RequestParam String fileName,
        Authentication authentication
        ) {
        MemberAuthEntity member = getMemberFromAuth(authentication);

        storageService.delete(fileName, member);
        return SuccessResponse.ok();
    }


}
