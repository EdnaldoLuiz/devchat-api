package com.ednaldoluiz.websocket.web.web.config;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ednaldoluiz.websocket.shared.constants.BeanConstants;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.experimental.FieldDefaults;

@Configuration
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class SwaggerConfig {

    static String BEARER_KEY = "bearer-key";
    static String BEARER = "bearer";
    static String JWT = "JWT";
    static String API_TITLE = "DevChat API";
    static String API_VERSION = "1.0.0";
    static String CONTACT_NAME = "Ednaldo Luiz";
    static String CONTACT_EMAIL = "contatoednaldoluiz@gmail.com";
    static String CONTACT_URL = "https://ednaldo-luiz.vercel.app";
    static String LICENSE_NAME = "Licença MIT";
    static String LICENSE_URL = "https://github.com/EdnaldoLuiz/movie-analytics/blob/main/LICENSE";
    static String LOCALHOST = "http://localhost:";
    static String API_DESCRIPTION = "<p>O DevChat é uma API moderna e escalável para um sistema de mensagens instantâneas, projetado para oferecer comunicação segura, eficiente e em tempo real. Ele permite que usuários interajam de maneira fluida e confiável, com recursos avançados de gerenciamento de mensagens, segurança e controle de acessos.</p>"
    + "<ul>"
    +   "<li><strong>Mensagens em tempo real:</strong> Utilize WebSockets para troca instantânea de mensagens, suportando tanto conversas individuais quanto grupos.</li>"
    +   "<li><strong>Criação e gerenciamento de grupos:</strong> Usuários podem criar grupos, definir administradores e gerenciar permissões de participação.</li>"
    +   "<li><strong>Sistema de autenticação robusto:</strong> Login seguro com tokens JWT, suporte a autenticação multifator (MFA) e criptografia de senhas.</li>"
    +   "<li><strong>Notificações e status de leitura:</strong> Indicação de mensagens entregues e lidas, além de notificações push para manter os usuários sempre informados.</li>"
    +   "<li><strong>Armazenamento seguro de mídias:</strong> Suporte para envio de imagens, vídeos e documentos com integração a AWS S3 e controle de acessibilidade.</li>"
    +   "<li><strong>Sistema de redefinição de senha seguro:</strong> Processo aprimorado de recuperação de senha, utilizando chaves únicas e tokens criptografados.</li>"
    +   "<li><strong>Camada de segurança avançada:</strong> Proteções contra ataques como brute force e rate limiting via Resilience4j, além de logs centralizados em AWS CloudWatch.</li>"
    +   "<li><strong>Gerenciamento de dispositivos:</strong> Controle e monitoramento dos dispositivos conectados à conta do usuário, permitindo desconexão remota.</li>"
    +   "<li><strong>Estrutura modular e escalável:</strong> Arquitetura baseada nos princípios do Clean Architecture e SOLID, garantindo manutenibilidade e crescimento sustentável.</li>"
    + "</ul>"
    + "<p>A API do DevChat foi projetada para atender tanto aplicações móveis quanto web, garantindo alta disponibilidade e um desempenho otimizado. Com suporte a integração via WebSockets e RESTful endpoints bem documentados, ela proporciona uma experiência de desenvolvimento fluída e eficiente.</p>";

    static TreeMap<String, String> SERVERS = new TreeMap<>();

    static {
        SERVERS.put(LOCALHOST + 8080, "Servidor Local");
        SERVERS.put(LOCALHOST + 8081, "Servidor Docker");
        SERVERS.put(LOCALHOST + 8082, "Servidor Produção");
    }

    @Bean(name = BeanConstants.SWAGGER_OPEN_API)
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(getComponents()) // Adiciona o esquema de autenticação
                .info(getApiInfo()) // Configuração de informações da API
                .addSecurityItem(new SecurityRequirement().addList(BEARER_KEY)) // Aplica autenticação JWT
                .servers(getServers()); // Adiciona os servidores
    }

    private Components getComponents() {
        return new Components()
                .addSecuritySchemes(BEARER_KEY, new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme(BEARER)
                        .bearerFormat(JWT)
                        .in(SecurityScheme.In.HEADER)
                        .name("Authorization")
                        .description("Bearer JWT token"));
    }

    private Info getApiInfo() {
        return new Info()
                .title(API_TITLE)
                .description(API_DESCRIPTION)
                .version(API_VERSION)
                .contact(getContact())
                .license(getLicense());
    }

    private Contact getContact() {
        return new Contact()
                .name(CONTACT_NAME)
                .email(CONTACT_EMAIL)
                .url(CONTACT_URL);
    }

    private List<Server> getServers() {
        List<Server> servers = new ArrayList<>();
        SERVERS.forEach((url, description) -> servers.add(new Server()
                .url(url)
                .description(description)));
        return servers;
    }

    private License getLicense() {
        return new License()
                .name(LICENSE_NAME)
                .url(LICENSE_URL);
    }
}