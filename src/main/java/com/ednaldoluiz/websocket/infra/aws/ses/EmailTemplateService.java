package com.ednaldoluiz.websocket.infra.aws.ses;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
public class EmailTemplateService {

    private final SpringTemplateEngine templateEngine;

    public EmailTemplateService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String buildPasswordResetEmail(String resetLink, String userName) {
        Context context = new Context();
        context.setVariable("resetLink", resetLink);
        context.setVariables(Map.of(
            "resetLink", resetLink,
            "userName", userName
        ));
        return templateEngine.process("password-reset.html", context);
    }
}