@/features/auth
Feature: Recuperação de Senha
  Como um usuário do sistema
  Eu quero solicitar um reset de senha
  Para redefinir minha senha caso eu esqueça

  @reset_valido @positive
  Scenario: Solicitar reset de senha com email cadastrado
    Given que existe um usuário pré-cadastrado com email "exemplo@teste.com" e senha "SenhaForte123!"
    And que existe um payload de recuperação para o email "exemplo@teste.com"
    When eu envio uma requisição de redefinição de senha com um email válido
    Then o status da resposta da API de recuperação deve ser sucesso 200
    And a resposta de sucesso deve conter a mensagem "E-mail de redefinição de senha enviado com sucesso"

  @reset_email_invalido @negative
  Scenario: Solicitar reset de senha com email inexistente
    Given que existe um payload de recuperação para o email "inexistente@teste.com"
    When eu envio uma requisição de redefinição de senha com um email inválido
    Then o status da resposta da API de recuperação deve ser um erro 404
    And a resposta de erro deve conter a mensagem "O e-mail informado não está cadastrado."