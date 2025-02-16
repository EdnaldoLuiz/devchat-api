<h1 align=center>DevChat-Api</h1>

<h2 id="tech-stack">🛠️ Tech Stack</h2>

<h3 id="tech-stack">🛠️ Tecnologies</h2>

<table align="center" width=1000px>
    <thead>
        <tr>
            <th><img src="https://skills-icons.vercel.app/api/icons?i=mysql" width=100px height=100px/></th>
            <th><img src="https://skills-icons.vercel.app/api/icons?i=hibernate" width=100px height=100px/></th>
            <th><img src="https://skills-icons.vercel.app/api/icons?i=spring" width=100px height=100px/></th>
            <th><img src="https://skills-icons.vercel.app/api/icons?i=java" width=100px height=100px/></th>
            <th><img src="https://skills-icons.vercel.app/api/icons?i=redis" width=100px height=100px/></th>
            <th><img src="https://skills-icons.vercel.app/api/icons?i=jwt" width=100px height=100px/></th>
            <th><img src="https://skills-icons.vercel.app/api/icons?i=ws" width=100px height=100px/></th>
        </tr>
    </thead>
    <tbody align="center">
        <tr>
            <td>MySQL</td>
            <td>Hibernate</td>
            <td>Spring Boot</td>
            <td>Java</td>
            <td>Redis</td>
            <td>JWT</td>
            <td>WebSocket</td>
        </tr>
        <tr>
            <td>🔖 8.1.0</td>
            <td>🔖 6.3</td>
            <td>🔖 3.3.2</td>
            <td>🔖 17</td>
            <td>🔖 7.4.2</td>
            <td>🔖 0.12.6</td>
            <td>🔖 3.4.2</td>
        </tr>
    </tbody>
</table>

<h3 id="tech-stack">🛠️ Tools</h2>

<table align="center" width=1000px>
    <thead>
        <tr>
	    <th><img src="https://skills-icons.vercel.app/api/icons?i=datagrip" width=100px height=100px/></th>
            <th><img src="https://skills-icons.vercel.app/api/icons?i=intellij" width=100px height=100px/></th>
            <th><img src="https://skills-icons.vercel.app/api/icons?i=postman" width=100px height=100px/></th>
            <th><img src="https://skills-icons.vercel.app/api/icons?i=docker" width=100px height=100px/></th>
	        <th><img src="https://skills-icons.vercel.app/api/icons?i=maven" width=100px height=100px/></th>
        </tr>
    </thead>
    <tbody align="center">
        <tr>
	        <td>DataGrip</td>
            <td>IntelliJ</td>
            <td>Postman</td>
            <td>Docker</td>
            <td>Maven</td>
        </tr>
        <tr>
	        <td>🔖 2024.3</td>
            <td>🔖 2024.3.2</td>
            <td>🔖 11.32.1</td>
            <td>🔖 27.5.1</td>
            <td>🔖 3.9.6</td>
        </tr>
    </tbody>
</table>

<h3 id="tech-stack">🛠️ Bibliotecas de Destaque</h2>

Nesta seção, listamos as bibliotecas que fornecem funcionalidades críticas e diferenciadas para o nosso projeto, como documentação da API, geração e validação de JWT e regras avançadas de validação de senha. Bibliotecas padrões (como os starters do Spring Boot) não são listadas aqui.

#### Redis

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

#### Migrations

```xml
 <dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-mysql</artifactId>
</dependency>
```

#### Websocket

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```

#### springdoc-openapi-starter-webmvc-ui

Esta biblioteca integra o Swagger UI à nossa aplicação Spring Boot, permitindo a geração automática da documentação da API baseada nas anotações OpenAPI.

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.1.0</version>
</dependency>
```

#### JSON Web Token (jjwt)

Utilizada para criar, assinar e validar os tokens JWT que garantem a segurança e a autenticidade das requisições na nossa aplicação.

```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
</dependency>
```

#### Passay

Fornece regras avançadas para validação de senhas, garantindo que as senhas dos usuários atendam aos requisitos mínimos de complexidade e segurança.

```xml
<dependency>
    <groupId>org.passay</groupId>
    <artifactId>passay</artifactId>
    <version>1.6.6</version>
</dependency>
```

#### UserAgentUtils

Essa biblioteca é usada para extrair informações detalhadas sobre o navegador e dispositivo do usuário, permitindo personalizar a experiência e registrar estatísticas de acesso.

```xml
<dependency>
    <groupId>eu.bitwalker</groupId>
    <artifactId>UserAgentUtils</artifactId>
    <version>1.21</version>
</dependency>
```
