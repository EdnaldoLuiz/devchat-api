package com.ednaldoluiz.websocket.app.v1.auth.usecase.adapter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ednaldoluiz.websocket.domain.port.EmailSenderPort;


@SpringBootTest
public class SesEmailSenderIntegrationTest {

    @Autowired
    private EmailSenderPort sesEmailSender;

    @Test
    public void testSendEmail() {
        // Para testes, envia para contatoednaldoluiz@gmail.com
        String sender = sesEmailSender.getSenderEmail();
        String recipient = "contatoednaldoluiz@gmail.com";
        String subject = "Teste de Envio de Email";
        String bodyHtml = "<html><body>"
                + "<h1>Teste</h1>"
                + "<p>Este é um email de teste para ver se o envio está funcionando.</p>"
                + "</body></html>";

        // Esse método deve registrar nos logs (e enviar o email se estiver configurado corretamente).
        sesEmailSender.sendEmail(sender, recipient, subject, bodyHtml);
    }
}
