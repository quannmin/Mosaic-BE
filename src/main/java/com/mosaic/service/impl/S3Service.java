package com.mosaic.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.s3.product-image-folder}")
    private String productImageFolder;

    @Value("${aws.s3.product-variant-image-folder}")
    private String productVariantImageFolder;

    public String uploadProductImage(MultipartFile file) {
        return uploadFile(file, productImageFolder);
    }

    public String uploadProductVariantImage(MultipartFile file) {
        return uploadFile(file, productVariantImageFolder);
    }

    private String uploadFile(MultipartFile file, String folderPath) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            String uniqueFilename = UUID.randomUUID() + "." + extension;

            String s3Key = folderPath + uniqueFilename;

            Map<String, String> metadata = new HashMap<>();
            metadata.put("Content-Type", file.getContentType());
            metadata.put("Content-Length", String.valueOf(file.getSize()));

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .metadata(metadata)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, s3Key);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null) {
            return "";
        }
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1);
    }

    public void deleteImage(String imageUrl) {
        try {
            String s3key = extractS3KeyFromUrl(imageUrl);
            if(s3key == null) {
                log.error("Invalid S3 URL format: {}", imageUrl);
                return;
            }
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(bucketName).key(s3key).build();
            s3Client.deleteObject(deleteObjectRequest);
            log.info("Successfully deleted image from S3 {}", s3key);
        } catch (Exception e) {
            log.error("Error deleting S3 URL: {}", imageUrl, e);
            throw new RuntimeException("Failed to delete image from S3", e);
        }
    }

    private String extractS3KeyFromUrl(String imageUrl) {
        try {
            String expectedPrefix = String.format("https://%s.s3.%s.amazonaws.com/", bucketName, region);
            if(!imageUrl.startsWith(expectedPrefix)) {
                log.warn("URL does not match expected S3 bucket: {}", imageUrl);
                return null;
            }

            return imageUrl.substring(expectedPrefix.length());
        } catch (Exception e) {
            log.error("Error parsing S3 URL: {}", imageUrl, e);
            return null;
        }
    }
}
