@auth
Feature: Registro de Usuário
  Como um novo usuário
  Eu quero criar uma conta no sistema
  Para acessar recursos protegidos

    @registro_sucesso @positive
    Scenario: Registrar usuário com sucesso
      Given que existe um payload de registro válido
      When eu envio uma requisição de registro
      Then o status da resposta da API de registro deve ser sucesso 201
      And deve retornar o email do novo usuário e seus tokens

    @registro_email_duplicado @negative
    Scenario: Tentar registrar usuário com email já existente
        Given que existe um usuario pré-cadastrado com email "existinguser@example.com"
        And que existe um payload de registro com o email "existinguser@example.com"
        When eu envio uma requisição de registro
        Then o status da resposta da API de registro deve ser um erro 400
        And deve conter a mensagem "Já existe um usuário com este email."

    @registro_senhas_nao_coincidem @negative
    Scenario: Tentar registrar usuário com senhas diferentes
      Given que existe um payload de registro com senhas diferentes
      When eu envio uma requisição de registro
      Then o status da resposta da API de registro deve ser um erro 400
      And deve conter a mensagem "Senhas não conferem."

    @registro_senha_fraca @negative
    Scenario Outline: Tentar registrar usuário com senha fraca
      Given que existe um payload de registro com a senha "<senha>"
      When eu envio uma requisição de registro
      Then o status da resposta da API de registro deve ser um erro 400
      And deve conter a mensagem "<mensagem>"

      Examples:
        | senha    | mensagem                                      |
        | abcdefgh | Deve conter pelo menos 1 caractere maiúsculo. |
        | ABCDEFGH | Deve conter pelo menos 1 dígito.              |
        | Abcdefgh | Deve conter pelo menos 1 caractere especial.  |