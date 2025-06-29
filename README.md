<h1 align=center>DevChat-Api</h1>

## *🗺️ Diagramas*


### *🔒🔑 Arquitetura de Autenticação de Usuários*

Este diagrama mostra, de forma clara e enxuta, todo o fluxo de autenticação do sistema:

* **Registro** de novos usuários com senha criptografada
* **Login** por credenciais e via **OAuth2** (GitHub/Google)
* **Recuperação de senha** (esqueci senha + redefinição) sem expor existência de conta
* **Logout** via blacklist de token
* **Acesso a rotas protegidas** validando JWT e blacklist

```mermaid
---
config:
  theme: redux-dark-color
---
sequenceDiagram
  autonumber
  participant FE as Front-end
  participant API as REST API
  participant DB as MySQL
  participant REDIS as Redis (Blacklist)
  participant SES as Serviço de Email
  participant OAUTH as OAuth2 Provider

  %% 1) REGISTRO
  rect rgba(0, 0, 255, 0.1)
    FE->>API: POST /auth/register {email, senha, confirmação, nome, termos}
    API->>DB: cria usuário com senha criptografada
    DB-->>API: usuário criado (id)
    API-->API: gera JWT + Refresh
    API-->>FE: 201 Created {access, refresh}
  end

  %% 2) LOGIN POR CREDENCIAIS
  rect rgba(0, 128, 0, 0.1)
    FE->>API: POST /auth/login {email, senha}
    API->>DB: busca usuário por email e compare hash
    alt credenciais válidas
      API-->API: gera JWT + Refresh
      API-->>FE: 200 OK {access, refresh}
    else inválido
      API-->>FE: 401 Unauthorized {"Email ou senha inválidos"}
    end
  end

  %% 3) LOGIN VIA OAUTH2
  rect rgba(0, 128, 128, 0.1)
    FE->>OAUTH: redirect para login externo
    OAUTH-->>FE: Callback OAuth2 com ccódigo
    FE->>API: GET /auth/oauth2/success?code=…
    API-->API: troca código por userInfo
    API->>DB: busca ou cria usuário via provider
    API-->API: gera JWT
    API-->>FE: 200 OK {access}
  end

  %% 4) ESQUECI SENHA → RESET
  rect rgba(255, 165, 0, 0.1)
    FE->>API: POST /auth/forgot-password?email=…
    API->>DB: tenta buscar usuário por email
    alt email cadastrado
      API-->API: cria token (UUID + hash) e persiste
      API-->API: monta link de reset
      API->>SES: envia email com link
    else não cadastrado
      Note right of API: ignora e retorna mesma resposta
    end
    API-->>FE: 200 OK {"Confira seu email para instruções"}
  end

  rect rgba(255, 140, 0, 0.1)
    FE->>API: POST /auth/reset-password {key, token, novaSenha}
    API->>DB: busca token de reset por key
    alt token válido e não expirado
      API-->API: atualiza senha (hash) e marca token usado
      API-->>FE: 200 OK {"Senha redefinida com sucesso"}
    else inválido ou expirado
      API-->>FE: 400 Bad Request {"Token inválido ou expirado"}
    end
  end

  %% 5) LOGOUT
  rect rgba(220, 20, 60, 0.1)
    FE->>API: POST /auth/logout (Bearer JWT)
    API-->API: extrai jti + exp do token
    API->>REDIS: adiciona jti na blacklist até expirar
    REDIS-->>API: OK
    API-->>FE: 200 OK {"Logout realizado"}
  end

  %% 6) ACESSO A RECURSOS PROTEGIDOS
  rect rgba(128, 0, 128, 0.1)
    FE->>API: GET /api/recurso (Bearer JWT)
    API-->API: JwtAuthFilter intercepta
    API->>REDIS: verifica se jti está blacklist
    alt token válido e não blacklist
      API-->>FE: 200 OK {dados…}
    else inválido ou blacklist
      API-->>FE: 401 Unauthorized {"Token inválido ou expirado"}
    end
  end
```

---

### *🔒🔑 Arquitetura E2EE (Signal)*

Este diagrama ilustra o fluxo completo de comunicação **fim-a-fim criptografada** implementado na nossa API usando o protocolo Signal:

- Mostra como o cliente gera e publica chaves na primeira inicialização (ou quando o storage local é perdido).
- Descreve o handshake de sessão entre usuários, com fetch seguro de bundles.
- Explica o envio de mensagens cifradas via WebSocket, totalmente opacas ao servidor.
- Detalha como o backend monitora **pre-keys** e **signedPreKeys**, enviando alertas automáticos para reposição ou rotação.
- Representa a segurança baseada em **IndexedDB local** com _trust-on-first-use_.

---

```mermaid
---
config:
  theme: redux-dark-color
---
sequenceDiagram
  autonumber
  participant FE_A as Frontend A
  participant SC_A as SignalClient A
  participant WS as WebSocket Broker
  participant API as REST API
  participant FE_B as Frontend B
  participant SC_B as SignalClient B

  rect rgba(0, 0, 255, 0.1)
    FE_A->>SC_A: bootstrap()
    alt Primeiro login OU IndexedDB limpo/perdido
      SC_A->>API: POST /signal/keys/save
      API-->>SC_A: 201 Created
    else Já tem chaves locais
      SC_A-->SC_A: Load keys from IndexedDB
    end
  end

  rect rgba(0, 100, 0, 0.1)
    FE_A->>SC_A: ensureSession(B)
    SC_A->>API: GET /signal/keys/fetch/{B}
    API-->>SC_A: {identity_B, SPK_B, PK₁}
    SC_A-->SC_A: SessionBuilder.processPreKey()
  end

  rect rgba(128, 0, 128, 0.1)
    FE_A->>SC_A: encrypt("Olá")
    SC_A->>WS: SEND /chat (ciphertext, type=WHISPER)
    WS->>FE_B: MESSAGE (ciphertext)
    FE_B->>SC_B: decrypt()
    SC_B-->FE_B: "Olá"
  end

  rect rgba(255, 165, 0, 0.1)
    Note over API: Monitor de PreKeys
    API->>WS: toUser(B) {type: LOW_PREKEY, remaining:18}
    WS->>FE_B: evento LOW_PREKEY
    FE_B->>SC_B: replenishBundle() (+100 PK)
    SC_B->>API: POST /signal/keys/save (novos PKs)
    API-->>SC_B: 201 Created
  end

  rect rgba(220, 20, 60, 0.1)
    Note over API: Scheduler detecta SPK expirada
    API->>WS: toUser(B) {type: SPK_EXPIRED}
    WS->>FE_B: evento SPK_EXPIRED
    FE_B->>SC_B: rotateSignedPreKey()
    SC_B->>API: POST /signal/keys/rotate-spk (nova SPK₁)
    API-->>SC_B: 200 OK
  end

  Note over FE_A,FE_B: 🛡️ Próxima mensagem usa SPK₁ de B
```

---

## *🖥️ Tech Stack*

### *👨‍💻 Technologies*

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

---

### *🛠️ Development Tools*

<table align="center" width=1000px>
    <thead>
        <tr>
            <th><img src="https://skills-icons.vercel.app/api/icons?i=intellij" width=100px height=100px/></th>
            <th><img src="https://skills-icons.vercel.app/api/icons?i=datagrip" width=100px height=100px/></th>
            <th><img src="https://skills-icons.vercel.app/api/icons?i=postman" width=100px height=100px/></th>
            <th><img src="https://skills-icons.vercel.app/api/icons?i=docker" width=100px height=100px/></th>
            <th><img src="https://skills-icons.vercel.app/api/icons?i=maven" width=100px height=100px/></th>
            <th><img src="https://skills-icons.vercel.app/api/icons?i=githubactions" width=100px height=100px/></th>
        </tr>
    </thead>
    <tbody align="center">
        <tr>
            <td>IntelliJ IDEA</td>
            <td>DataGrip</td>
            <td>Postman</td>
            <td>Docker</td>
            <td>Maven</td>
            <td>GitHub Actions</td>
        </tr>
        <tr>
            <td>🔖 2024.3.2</td>
            <td>🔖 2024.3</td>
            <td>🔖 11.32.1</td>
            <td>🔖 27.5.1</td>
            <td>🔖 3.9.6</td>
            <td>🔖 - </td>
        </tr>
    </tbody>
</table>

---

### *☁️ Cloud Services*

<table align="center" width=1000px>
    <thead>
        <tr>
            <th><img src="https://icon.icepanel.io/AWS/svg/Storage/Simple-Storage-Service.svg" width=100px height=100px/></th>
            <th><img src="https://icon.icepanel.io/AWS/svg/Business-Applications/Simple-Email-Service.svg" width=100px height=100px/></th>
            <th><img src="https://icon.icepanel.io/AWS/svg/Management-Governance/CloudWatch.svg" width=100px height=100px/></th>
        </tr>
    </thead>
    <tbody align="center">
        <tr>
            <td>AWS S3</td>
            <td>AWS SES</td>
            <td>AWS CloudWatch</td>
        </tr>
    </tbody>
</table>

---

### *🧪 Testing & QA*

<table align="center" width=1000px>
    <thead>
        <tr>
            <th><img src="https://skills-icons.vercel.app/api/icons?i=swagger" width=100px height=100px/></th>
            <th><img src="https://skills-icons.vercel.app/api/icons?i=cucumber" width=100px height=100px/></th>
            <th><img src="https://skills-icons.vercel.app/api/icons?i=junit5" width=100px height=100px/></th>
            <th><img src="https://skills-icons.vercel.app/api/icons?i=archunit" width=100px height=100px/></th>
            <th><img src="https://skills-icons.vercel.app/api/icons?i=testcontainers" width=100px height=100px/></th>
        </tr>
    </thead>
    <tbody align="center">
        <tr>
            <td>Swagger</td>
            <td>Cucumber</td>
            <td>JUnit</td>
            <td>ArchUnit</td>
            <td>TestContainers</td>
        </tr>
        <tr>
            <td>🔖 2.8.5 </td>
            <td>🔖 7.21.1 </td>
            <td>🔖 5 </td>
            <td>🔖 1.4.0 </td>
            <td>🔖 1.20.4 </td>
        </tr>
    </tbody>
</table>
