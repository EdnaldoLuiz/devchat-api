@auth
Feature: Reset de Senha
  Como um usuário do sistema
  Eu quero redefinir minha senha
  Para que eu possa acessar novamente o sistema caso eu tenha esquecido

  @reset_sucesso @positive
  Scenario: Resetar senha com sucesso
    Given que existe um usuário cadastrado com email "user@example.com"
    And que existe um token válido com key "f2f72a41-6aaa-4c30-878f-5080aac043a2" e token "05cc54ce-74cf-4a89-b256-c42021f9bd3b" vinculado ao usuário com email "user@example.com"
    And que existe um payload de reset com key "f2f72a41-6aaa-4c30-878f-5080aac043a2", token "05cc54ce-74cf-4a89-b256-c42021f9bd3b", senha "SenhaNova123!" e confirmPassword "SenhaNova123!"
    When eu envio uma requisição de reset de senha
    Then o status da resposta da API de reset deve ser 200
    And deve conter a mensagem de reset "Senha redefinida com sucesso"

  @reset_token_inexistente @negative
  Scenario: Tentar resetar senha com token inexistente
    Given que existe um payload de reset com key "00000000-0000-0000-0000-000000000000", token "11111111-1111-1111-1111-111111111111", senha "SenhaNova123!" e confirmPassword "SenhaNova123!"
    When eu envio uma requisição de reset de senha
    Then o status da resposta da API de reset deve ser 400
    And deve conter a mensagem de reset "Token inválido ou expirado."

  @reset_token_expirado @negative
  Scenario: Tentar resetar senha com token expirado
    Given que existe um usuário cadastrado com email "user2@example.com"
    And que existe um token expirado com key "a1b2c3d4-e5f6-7890-1234-56789abcdef0" vinculado ao usuário
    And que existe um payload de reset com key "a1b2c3d4-e5f6-7890-1234-56789abcdef0", token "88888888-8888-8888-8888-888888888888", senha "SenhaNova123!" e confirmPassword "SenhaNova123!"
    When eu envio uma requisição de reset de senha
    Then o status da resposta da API de reset deve ser 400
    And deve conter a mensagem de reset "Token expirado ou já utilizado."

  @reset_token_invalido @negative
  Scenario: Tentar resetar senha com token que não corresponde ao hashedToken
    Given que existe um usuário cadastrado com email "user3@example.com"
    And que existe um token válido com key "00000000-0000-0000-0000-000000000000" e token "00000000-0000-0000-0000-000000000002" vinculado ao usuário com email "user3@example.com"
    And que existe um payload de reset com key "00000000-0000-0000-0000-000000000000", token "00000000-0000-0000-0000-000000000001", senha "SenhaNova123!" e confirmPassword "SenhaNova123!"
    When eu envio uma requisição de reset de senha
    Then o status da resposta da API de reset deve ser 400
    And deve conter a mensagem de reset "Token inválido."

  @reset_senhas_nao_conferem @negative
  Scenario: Tentar resetar senha com senhas diferentes
    Given que existe um usuário cadastrado com email "user4@example.com"
    And que existe um token válido com key "00000000-0000-0000-0000-000000000001" e token "00000000-0000-0000-0000-000000000001" vinculado ao usuário com email "user4@example.com"
    And que existe um payload de reset com key "00000000-0000-0000-0000-000000000002", token "00000000-0000-0000-0000-000000000002", senha "MinhaNovaSenha1+" e confirmPassword "MinhaNovaSenha2+"
    When eu envio uma requisição de reset de senha
    Then o status da resposta da API de reset deve ser 400
    And deve conter a mensagem de reset "Senhas não conferem."