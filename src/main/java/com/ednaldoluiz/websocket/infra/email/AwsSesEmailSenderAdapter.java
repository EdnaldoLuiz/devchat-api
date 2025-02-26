package com.ednaldoluiz.websocket.infra.email;

import com.ednaldoluiz.websocket.domain.port.EmailSenderPort;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Slf4j
@Component
public class AwsSesEmailSenderAdapter implements EmailSenderPort {

    private final SesClient sesClient;
    private final String defaultSender;

    public AwsSesEmailSenderAdapter(
            @Value("${aws.ses.region}") String region,
            @Value("${aws.ses.sender}") String defaultSender,
            @Value("${aws.ses.endpoint}") String endpoint,
            @Value("${aws.accessKeyId}") String accessKey,
            @Value("${aws.secretAccessKey}") String secretKey
    ) {
        this.sesClient = SesClient.builder()
            .region(Region.of(region))
            .credentialsProvider(
                StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey))
            )
            .build();
        this.defaultSender = defaultSender;
    }

    @Override
    public void sendEmail(String from, String to, String subject, String htmlBody) {
        log.info("Enviando e-mail de {} para {} com assunto: {}", from, to, subject);

        Destination destination = Destination.builder()
            .toAddresses(to)
            .build();

        Content sub = Content.builder().
            data(subject)
            .build();
            
        Content html = Content.builder()
            .data(htmlBody)
            .build();

        Body body = Body.builder()
            .html(html)
            .build();

        Message message = Message.builder()
            .subject(sub)
            .body(body)
            .build();

        SendEmailRequest emailRequest = SendEmailRequest.builder()
            .destination(destination)
            .message(message)
            .source(defaultSender)
            .build();

        try {
            sesClient.sendEmail(emailRequest);
            log.info("E-mail enviado com sucesso!");
        } catch (SesException e) {
            throw new RuntimeException("Falha ao enviar e-mail via SES: " + e.awsErrorDetails().errorMessage(), e);
        }
    }
}