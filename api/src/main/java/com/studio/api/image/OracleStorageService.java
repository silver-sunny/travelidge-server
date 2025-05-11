package com.studio.api.image;

import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.DeleteObjectRequest;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.studio.core.member.entity.MemberAuthEntity;
import jakarta.annotation.PreDestroy;
import java.io.BufferedInputStream;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class OracleStorageService {

    private static final String namespace = "axl6tdfifug3"; // 콘솔에서 확인
    private static final String bucketName = "bucket-ticket";

    private final ObjectStorageClient client;
    private final String regionId;

    public OracleStorageService(ObjectStorageClient client, String regionId) {
        this.client = client;
        this.regionId = regionId;
    }


    @PreDestroy
    public void cleanUp() throws Exception {
        if (client != null) {
            client.close();
        }
    }

    public String upload(MultipartFile file, MemberAuthEntity member) throws Exception {

        String objectName =
            member.getMemberNo() + "/" + generateUniqueFileName(file.getOriginalFilename());

        BufferedInputStream bufferedInputStream = new BufferedInputStream(file.getInputStream());

        PutObjectRequest request = PutObjectRequest.builder()
            .namespaceName(namespace)
            .bucketName(bucketName)
            .objectName(objectName)
            .putObjectBody(bufferedInputStream)
            .contentLength(file.getSize())
            .build();

        client.putObject(request);

        return generatePublicUrl(objectName);
    }

    // 고유 파일명 생성
    private String generateUniqueFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        return uuid + "_" + originalFilename;
    }


    public void delete(String fileName, MemberAuthEntity member) {

        DeleteObjectRequest request = DeleteObjectRequest.builder()
            .namespaceName(namespace)
            .bucketName(bucketName)
            .objectName(member.getMemberNo() + "/" + fileName)
            .build();

        client.deleteObject(request);
    }

    private String generatePublicUrl(String fileName) {
        return String.format("https://%s.objectstorage.%s.oci.customer-oci.com/n/%s/b/%s/o/%s",
            namespace, regionId, namespace, bucketName, fileName);
    }
}
