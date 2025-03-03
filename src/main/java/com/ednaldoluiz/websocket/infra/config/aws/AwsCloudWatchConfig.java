package com.ednaldoluiz.websocket.infra.config.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.experimental.FieldDefaults;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;

@Configuration
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AwsCloudWatchConfig {

    @Value("${aws.accessKeyId}") 
    String accessKey;
            
    @Value("${aws.secretAccessKey}") 
    String secretKey;

    @Value("${aws.region}")
    String region;

    @Bean
    public CloudWatchClient cloudWatchClient() {
        return CloudWatchClient.builder()
            .region(Region.of(region))
            .credentialsProvider(
                StaticCredentialsProvider.create(AwsBasicCredentials.create(
                    accessKey, secretKey
                ))
            )
            .build();
    }
}