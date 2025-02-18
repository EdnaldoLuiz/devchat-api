package com.ednaldoluiz.websocket.infra.email;

import com.ednaldoluiz.websocket.domain.port.EmailSenderPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

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
            .endpointOverride(java.net.URI.create(endpoint))
            .credentialsProvider(
                StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey))
            )
            .build();

        // Opcional: verificar e-mail (apenas se estiver usando LocalStack ou quiser forçar verificação).
        sesClient.verifyEmailIdentity(VerifyEmailIdentityRequest.builder()
            .emailAddress(defaultSender)
            .build());

        this.defaultSender = defaultSender;
    }

    @Override
    public void sendEmail(String from, String to, String subject, String htmlBody) {
        // Se preferir forçar o "from" como defaultSender, basta usar defaultSender aqui.
        String actualFrom = (from != null) ? from : defaultSender;

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
            .source(actualFrom)
            .build();

        try {
            sesClient.sendEmail(emailRequest);
        } catch (SesException e) {
            throw new RuntimeException("Falha ao enviar e-mail via SES: "
                + e.awsErrorDetails().errorMessage(), e);
        }
    }
}
