package com.studio.core.global.naver.file;

import com.studio.core.global.exception.CustomException;
import com.studio.core.global.naver.dto.ImagesResponseDto;
import com.studio.core.global.naver.service.NaverApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.studio.core.global.exception.ErrorCode.FILE_CREATE_ERROR;

@Service
@RequiredArgsConstructor
public class FileService {

    private final NaverApiService naverApiService;

    public static final String INSERT_PRODUCT_IMAGES_URL = "https://api.commerce.naver.com/external/v1/product-images/upload";


    private MultiValueMap<String, HttpEntity<?>> buildMultipartRequestBody(List<MultipartFile> imageFiles) throws IOException {
        LinkedMultiValueMap<String, HttpEntity<?>> multipartData = new LinkedMultiValueMap<>();

        for (MultipartFile file : imageFiles) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(file.getContentType()));
            headers.setContentDispositionFormData("imageFiles", URLEncoder.encode(file.getOriginalFilename(), StandardCharsets.UTF_8));
            HttpEntity<byte[]> fileEntity = new HttpEntity<>(file.getBytes(), headers);
            multipartData.add("imageFiles", fileEntity);
        }
        return multipartData;
    }



    public ImagesResponseDto uploadImages(List<MultipartFile> imageFiles) {
        try {
            MultiValueMap<String, HttpEntity<?>> multipartData = buildMultipartRequestBody(imageFiles);

            return naverApiService.sendRequest(
                INSERT_PRODUCT_IMAGES_URL,
                HttpMethod.POST,
                multipartData,
                ImagesResponseDto.class
            ).block();
        } catch (IOException e) {
            throw new CustomException(FILE_CREATE_ERROR);
        }
    }

}
