package com.studio.api.file.controller;


import com.studio.core.global.naver.dto.ImagesResponseDto;
import com.studio.core.global.naver.file.FileService;
import com.studio.core.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/api/client")
@Tag(name = "file-controller", description = "FILE API")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;



    @Operation(summary = "이미지 등록", description = "")
    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SuccessResponse<ImagesResponseDto> uploadNaverProductImages(
        @RequestPart("file") List<MultipartFile> files) {

        return SuccessResponse.ok(fileService.uploadImages(files));
    }



}
