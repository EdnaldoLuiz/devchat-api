package com.ednaldoluiz.websocket.infra.web.docs;

public final class AuthControllerDocs {

    private AuthControllerDocs() {}

    public static final String REGISTER_SUMMARY = "Registro de Usuário";

    public static final String REGISTER_DESCRIPTION = """
        <html>
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
                            <td>@Email, @NotBlank</td>
                            <td>Email do usuário.</td>
                            <td>luiz@gmail.com</td>
                            <td>Deve ser um email válido e não pode estar em branco, contendo "@" e um "." após o domínio.</td>
                        </tr>
                        <tr>
                            <td><strong>password</strong></td>
                            <td>@NotBlank, @Size(min=8, max=20)</td>
                            <td>Senha do usuário.</td>
                            <td>Abcde123+</td>
                            <td>Deve ter entre 8 e 20 caracteres, conter ao menos 1 minúsculo, 1 maiúsculo, 1 número e 1 caractere especial.</td>
                        </tr>
                        <tr>
                            <td><strong>name</strong></td>
                            <td>@NotBlank, @Size(min=3, max=50)</td>
                            <td>Nome do usuário.</td>
                            <td>Luiz</td>
                            <td>Deve ter entre 3 e 50 caracteres e não pode estar em branco.</td>
                        </tr>
                        <tr>
                            <td><strong>phone</strong></td>
                            <td>@NotBlank, @Phone</td>
                            <td>Número de telefone do usuário.</td>
                            <td>11912345678</td>
                            <td>Deve ter 10 ou 11 dígitos, iniciar com um DDD válido e não pode ser repetido.</td>
                        </tr>
                        <tr>
                            <td><strong>avatar</strong></td>
                            <td>-</td>
                            <td>URL do avatar do usuário.</td>
                            <td>https://meusite.com/avatar.png</td>
                            <td>Opcional, pode ser nulo.</td>
                        </tr>
                        <tr>
                            <td><strong>terms</strong></td>
                            <td>@AssertTrue</td>
                            <td>Confirmação dos termos de uso.</td>
                            <td>true</td>
                            <td>Deve ser `true`, caso contrário, o registro não será permitido.</td>
                        </tr>
                    </tbody>
                </table>
            </body>
        </html>
        """;
}