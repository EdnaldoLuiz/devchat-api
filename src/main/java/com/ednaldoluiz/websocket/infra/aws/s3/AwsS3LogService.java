package com.ednaldoluiz.websocket.infra.aws.s3;

import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AwsS3LogService {

    S3Client s3Client;
    String bucketName;

    public AwsS3LogService(
            S3Client s3Client,
            @Value("${aws.s3.bucket-name}") String bucketName
    ) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public void saveLog(String logType, String content) {
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String fileName = logType + "-" + UUID.randomUUID() + ".log";

        String objectKey = String.format("%s/%s", datePath, fileName);

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .contentType("text/plain")
                    .build();

            s3Client.putObject(request, software.amazon.awssdk.core.sync.RequestBody.fromBytes(content.getBytes(StandardCharsets.UTF_8)));

            log.info("Log salvo no S3: {}/{}", bucketName, objectKey);
        } catch (S3Exception e) {
            log.error("Erro ao salvar log no S3: {}", e.awsErrorDetails().errorMessage());
        }
    }
}