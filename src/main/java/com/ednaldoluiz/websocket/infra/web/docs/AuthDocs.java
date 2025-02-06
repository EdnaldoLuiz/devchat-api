package com.ednaldoluiz.websocket.infra.web.docs;

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
                <body>
                    <h3>Detalhes do RegisterRequest</h3>
                    <table cellpadding="5" cellspacing="0">
                        <thead>
                            <tr>
                                <th>Campo</th>
                                <th>Validado por</th>
                                <th>Descrição</th>
                                <th>Exemplo</th>
                                <th>Regras de Validação</th>
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
                                <td><strong>name</strong></td>
                                <td>`@NotBlank`, `@Size(min=3, max=50)`</td>
                                <td>Nome do usuário.</td>
                                <td>Luiz</td>
                                <td>Deve ter entre 3 e 50 caracteres e não pode estar em branco.</td>
                            </tr>
                            <tr>
                                <td><strong>phone</strong></td>
                                <td>`@NotBlank`, `@Phone`</td>
                                <td>Número de telefone do usuário.</td>
                                <td>11912345678</td>
                                <td>Deve ter 10 ou 11 dígitos, iniciar com um DDD válido e não pode ser repetido.</td>
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
            """;
            String STATUS_201_RESPONSE = """
                {
                    "userId": 123456789,
                    "email": "usuario@email.com",
                    "token": "eyJhbGciOiJIUzI1...",
                    "refreshToken": "eyJhbGciOiJIUzI1..."
                }
            """;
            String STATUS_400_RESPONSE = """
                {
                    "status": 400,
                    "error": "Validation Error",
                    "message": "Erros de validação encontrados.",
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
                <body>
                    <h3>Detalhes do LoginRequest</h3>
                    <table cellpadding="5" cellspacing="0">
                        <thead>
                            <tr>
                                <th>Campo</th>
                                <th>Validado por</th>
                                <th>Descrição</th>
                                <th>Exemplo</th>
                                <th>Regras de Validação</th>
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
        """;
        String STATUS_200_RESPONSE = "null";
        String STATUS_400_RESPONSE = "null";
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
}