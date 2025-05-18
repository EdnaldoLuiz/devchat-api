package com.ednaldoluiz.websocket.web.controller.v1.auth;

/**
 * Documentação dos endpoints de autenticação.
 *  - Register: Documentação para o endpoint de Registro de Usuário.
 *  - Login: Documentação para o endpoint de Login de Usuário.
 *  - GeneratePassword: Documentação para o endpoint de geração de senha segura.
 *  - Logout: Documentação para o endpoint de logout de usuário.
 */
public interface AuthDocs {

    /**
     * Documentação para o endpoint de Registro de Usuário.
     */

    interface Register {
        String SUMMARY = "Registro de Usuário";

        String DESCRIPTION = """
            <html>
                <body>
                    <h3>Detalhes do RegisterRequest</h3>
                    <table cellpadding="5" cellspacing="0">
                        <thead>
                            <tr>
                                <th width="10%">Campo</th>
                                <th width="25%">Validado por</th>
                                <th width="20%">Descrição</th>
                                <th width="15%">Exemplo</th>
                                <th width="30%">Regras de Validação</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td><strong>email</strong></td>
                                <td>`@Email`, `@NotBlank`</td>
                                <td>Email do usuário.</td>
                                <td>luiz@gmail.com</td>
                                <td>Deve ser um email válido e não pode estar em branco, contendo "@" e um "." após o domínio.</td>
                            </tr>
                            <tr>
                                <td><strong>password</strong></td>
                                <td>`@NotBlank`,`@Size(min=8, max=20)`</td>
                                <td>Senha do usuário.</td>
                                <td>Abcde123+</td>
                                <td>Deve ter entre 8 e 20 caracteres, conter ao menos 1 minúsculo, 1 maiúsculo, 1 número e 1 caractere especial.</td>
                            </tr>
                            <tr>
                                <td><strong>confirmPassword</strong></td>
                                <td>`@NotBlank`,`@Size(min=8, max=20)`</td>
                                <td>Confirmação da senha do usuário.</td>
                                <td>Abcde123+</td>
                                <td>Deve ser igual à senha informada.</td>
                            </tr>
                            <tr>
                                <td><strong>name</strong></td>
                                <td>`@NotBlank`, `@Size(min=3, max=50)`</td>
                                <td>Nome do usuário.</td>
                                <td>Luiz</td>
                                <td>Deve ter entre 3 e 50 caracteres e não pode estar em branco.</td>
                            </tr>
                            <tr>
                                <td><strong>terms</strong></td>
                                <td>`@AssertTrue`</td>
                                <td>Confirmação dos termos de uso.</td>
                                <td>true</td>
                                <td>Deve ser `true`, caso contrário, o registro não será permitido.</td>
                            </tr>
                        </tbody>
                    </table>
                </body>
            </html>
            """;

            String STATUS_201_RESPONSE = """
                {
                    "userId": 123456789,
                    "email": "usuario@email.com",
                    "tokens": [
                        {
                            "type": "ACCESS",
                            "value": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sImp0aSI6IjE1OWFiODRlLWEyNGUtNGRlOC1hMTg0LTYwYjgzNTZmZDYzYiIsInN1YiI6InVzdWFyaW9AZW1haWwuY29tIiwiaWF0IjoxNzM4ODA1MzU1LCJleHAiOjE3Mzg4OTE3NTV9.k6-OFtPj1JDGgo9uIN7TjjpwlJscF35L15CjFpqRyNA"
                        },
                        {
                            "type": "REFRESH",
                            "value": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sImp0aSI6ImZlNGNhNDAyLWIzMzctNDQyMC1iMWNhLWI5Nzc0OGE3YTcwZiIsInN1YiI6InVzdWFyaW9AZW1haWwuY29tIiwiaWF0IjoxNzM4ODA1MzU1LCJleHAiOjE3Mzg4OTE3NTV9.qEQ_QLt3OFxaJ0Q_gvWOCE0LZ-lThCXufvaejiZCEaQ"
                        }
                    ]
                }
            """;
            String STATUS_400_RESPONSE = """
                {
                    "timestamp": "2025-02-05 12:30:45",
                    "status": 400,
                    "error": "Erro de validação com as credenciais do usuário",
                    "message": "Erros de validação encontrados.",
                    "path": "/api/v1/auth/register",
                    "fieldErrors": [
                        {"field": "email", "message": "O email informado é inválido."},
                        {"field": "password", "message": "A senha deve ter entre 8 e 20 caracteres."}
                    ]
                }
            """;
    }

    public interface Login {
        String SUMMARY = "Login de Usuário";
        
        String DESCRIPTION = """
            <html>
                <body>
                    <h3>Detalhes do LoginRequest</h3>
                    <table cellpadding="5" cellspacing="0">
                        <thead>
                            <tr>
                                <th width="10%">Campo</th>
                                <th width="25%">Validado por</th>
                                <th width="20%">Descrição</th>
                                <th width="15%">Exemplo</th>
                                <th width="30%">Regras de Validação</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td><strong>email</strong></td>
                                <td>`@Email`, `@NotBlank`</td>
                                <td>Email do usuário.</td>
                                <td>luiz@gmail.com</td>
                                <td>Deve ser um email válido e não pode estar em branco, contendo "@" e um "." após o domínio.</td>
                            </tr>
                            <tr>
                                <td><strong>password</strong></td>
                                <td>`@NotBlank`,`@Size(min=8, max=20)`</td>
                                <td>Senha do usuário.</td>
                                <td>Abcde123+</td>
                                <td>Deve ter entre 8 e 20 caracteres, conter ao menos 1 minúsculo, 1 maiúsculo, 1 número e 1 caractere especial.</td>
                            </tr>
                        </tbody>
                    </table>
                </body>
            </html>""";

        String STATUS_200_RESPONSE = """
            {
                "id": 123456789
                "email": "usuario@email.com",
                "name": "Usuário",
                "avatar": "https://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50x",
                "tokens": [
                    {
                        "type": "ACCESS",
                        "value": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sImp0aSI6IjE1OWFiODRlLWEyNGUtNGRlOC1hMTg0LTYwYjgzNTZmZDYzYiIsInN1YiI6InVzdWFyaW9AZW1haWwuY29tIiwiaWF0IjoxNzM4ODA1MzU1LCJleHAiOjE3Mzg4OTE3NTV9.k6-OFtPj1JDGgo9uIN7TjjpwlJscF35L15CjFpqRyNA"
                    },
                    {
                        "type": "REFRESH",
                        "value": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sImp0aSI6ImZlNGNhNDAyLWIzMzctNDQyMC1iMWNhLWI5Nzc0OGE3YTcwZiIsInN1YiI6InVzdWFyaW9AZW1haWwuY29tIiwiaWF0IjoxNzM4ODA1MzU1LCJleHAiOjE3Mzg4OTE3NTV9.qEQ_QLt3OFxaJ0Q_gvWOCE0LZ-lThCXufvaejiZCEaQ"
                    }
                ]
            }
        """;

        String STATUS_401_RESPONSE = """
            {
                "timestamp": "2025-02-05 12:30:45",
                "status": 401,
                "error": "Não autorizado",
                "message": "Email ou senha inválidos.",
                "path": "/api/v1/auth/login"
            }
        """;
    }

    /**
     * Documentação para o endpoint de geração de senha segura.
     */

     interface GeneratePassword {
        String SUMMARY = "Geração de Senha Segura";
        String DESCRIPTION = """
            <html>
                <body>
                    <h3>Geração de Senha Automática</h3>
                    <p>Este endpoint gera uma senha forte baseada nas regras de segurança do sistema.</p>
                    <ul>
                        <li>Comprimento: 8 a 20 caracteres</li>
                        <li>Deve conter pelo menos:
                            <ul>
                                <li>1 letra maiúscula</li>
                                <li>1 letra minúscula</li>
                                <li>1 número</li>
                                <li>1 caractere especial permitido (!@#$%^&*-_+=<>?)</li>
                            </ul>
                        </li>
                        <li>Não pode conter palavras proibidas (ex: "password", "123456")</li>
                        <li>É gerado um score de força baseado na complexidade da senha</li>
                    </ul>
                </body>
            </html>
            """;

        String STATUS_200_RESPONSE = """
            {
                "password": "V^sWXF0ibfNzo^-Ib!i?",
                "strengthScore": 100
            }
        """;
    }

    /**
     * Documentação para o endpoint de logout de usuário.
     */

    interface Logout {
        String SUMMARY = "Logout de Usuário";
        String DESCRIPTION = """
            <html>
                <body>
                    <h3>Logout de Usuário</h3>
                    <p>Este endpoint realiza o logout de um usuário autenticado.</p>
                    <p>Para realizar o logout, é necessário enviar o token de acesso do usuário.</p>
                </body>
            </html>
            """;

        String STATUS_200_RESPONSE = """
            {
                "timestamp": "2025-02-05 12:30:45",
                "status": 200,
                "message": "Logout realizado com sucesso.",
                "path": "/api/v1/auth/logout"
            }
        """;
    }

    interface ForgotPassword {
        String SUMMARY = "Recuperação de Senha";
        String DESCRIPTION = """
            <html>
                <body>
                    <h3>Recuperação de Senha</h3>
                    <p>Este endpoint permite que um usuário solicite a redefinição de senha. Se o email existir, um email será enviado com um link para redefinir a senha.</p>
                </body>
            </html>
            """;

        String STATUS_200_RESPONSE = """
            {
                "message": "Email de redefinição enviado se usuário existir"
            }
        """;
    }

    public interface ResetPassword {
        String SUMMARY = "Redefinição de Senha";
        
        String DESCRIPTION = """
            <html>
                <body>
                    <h3>Detalhes do ResetPasswordRequest</h3>
                    <p>Este endpoint permite que um usuário redefina sua senha após receber um token de redefinição.</p>
                    <table cellpadding="5" cellspacing="0">
                        <thead>
                            <th width="10%">Campo</th>
                            <th width="25%">Validado por</th>
                            <th width="25%">Descrição</th>
                            <th width="20%">Exemplo</th>
                            <th width="20%">Regras de Validação</th>
                        </thead>
                        <tbody>
                            <tr>
                                <td><strong>key</strong></td>
                                <td>`@NotBlank`</td>
                                <td>Chave única associada à solicitação de redefinição.</td>
                                <td>c7b3b3b3-4b3b-4b3b-4b3b-4b3b3b3b3b3b</td>
                                <td>Não pode estar em branco. Deve ser um UUID válido.</td>
                            </tr>
                            <tr>
                                <td><strong>token</strong></td>
                                <td>`@NotBlank`</td>
                                <td>Token de autenticação temporário para a redefinição.</td>
                                <td>f3d3a3a3-4d4d-4d4d-8e3d-4b3b3b3b3b3b</td>
                                <td>Não pode estar em branco. Deve ser um UUID válido.</td>
                            </tr>
                            <tr>
                                <td><strong>password</strong></td>
                                <td>`@NotBlank`, `@Size(min=8, max=20)`</td>
                                <td>Nova senha do usuário.</td>
                                <td>SenhaForte123!</td>
                                <td>Deve ter entre 8 e 20 caracteres, conter ao menos 1 minúscula, 1 maiúscula, 1 número e 1 caractere especial.</td>
                            </tr>
                            <tr>
                                <td><strong>confirmPassword</strong></td>
                                <td>`@NotBlank`, `@Size(min=8, max=20)`</td>
                                <td>Confirmação da nova senha.</td>
                                <td>SenhaForte123!</td>
                                <td>Deve ter entre 8 e 20 caracteres e ser idêntico ao campo `password`.</td>
                            </tr>
                        </tbody>
                    </table>
                </body>
            </html>
            """;    

        String STATUS_200_RESPONSE = """
            {
                "message": "Senha redefinida com sucesso"
            }
        """;
    }
}