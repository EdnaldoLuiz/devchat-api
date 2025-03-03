package com.ednaldoluiz.websocket.infra.aws.ses;

import com.ednaldoluiz.websocket.domain.port.EmailPort;
import com.ednaldoluiz.websocket.infra.aws.s3.AwsS3LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsyncAwsSesEmailAdapter implements EmailPort {

    private final SesClient sesClient;
    private final AwsSesMetricsService emailMetricsService;
    private final AwsS3LogService s3LogService;

    @Value("${aws.ses.sender}")
    private String defaultSender;

    @Value("${aws.ses.supportEmail}")
    private String supportEmail;

    @Value("${aws.ses.messageConfigurationSetName}")
    private String messageConfigurationSetName;

    /**
     * Envia um e-mail de forma assíncrona utilizando AWS SES.
     *
     * @param from    Remetente (opcional)
     * @param to      Destinatário
     * @param subject Assunto
     * @param htmlBody Corpo do e-mail em HTML
     * @return CompletableFuture indicando sucesso ou falha.
     */
    @Override
    @Async("customTaskExecutor")
    public void sendEmail(String from, String to, String subject, String htmlBody) {
        CompletableFuture.runAsync(() -> {
            try {
                log.info("Enviando e-mail para {} com assunto: {}", to, subject);
                sendEmailInternal(to, subject, htmlBody);
                emailMetricsService.publishEmailMetric("EmailsSent", 1);

                s3LogService.saveLog("email-success",String.format("Email enviado para %s com assunto: %s", to, subject));
                log.info("E-mail enviado com sucesso!");
            } catch (SesException e) {
                log.error("Erro ao enviar e-mail para {}: {}", to, e.awsErrorDetails().errorMessage(), e);
                emailMetricsService.publishEmailMetric("EmailsFailed", 1);
                s3LogService.saveLog("email-failure", String.format("Erro ao enviar e-mail para %s: %s", to, e.awsErrorDetails().errorMessage()));
                throw new RuntimeException("Falha ao enviar e-mail via SES", e);
            }
        });
    }

    private void sendEmailInternal(String to, String subject, String htmlBody) {
        String formattedSender = String.format("DevChat <%s>", this.defaultSender);

        Destination destination = Destination.builder()
                .toAddresses(to)
                .build();

        Content sub = Content.builder()
                .data(subject)
                .charset(StandardCharsets.UTF_8.name())
                .build();

        Content html = Content.builder()
                .data(htmlBody)
                .charset(StandardCharsets.UTF_8.name())
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
                .source(formattedSender)
                .configurationSetName(messageConfigurationSetName)
                .replyToAddresses(supportEmail)
                .returnPath(supportEmail)
                .tags(
                        MessageTag.builder().name("environment").value("production").build()
                )
                .build();

        sesClient.sendEmail(emailRequest);
    }
}