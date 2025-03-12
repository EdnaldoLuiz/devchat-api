Feature: Registro de Usuário
  Como um novo usuário
  Eu quero criar uma conta no sistema
  Para acessar recursos protegidos

  Background: Dado que eu limpei o banco de dados para garantir um estado inicial

  Rule: Registro de usuário com sucesso

    @registro_sucesso
    Scenario: Registrar usuário com sucesso
      Dado que existe um payload de registro válido
      Quando eu envio uma requisição de registro
      Então a resposta deve ser de sucesso com status 201
      E deve retornar o email do novo usuário e seus tokens

  Rule: Validação de registro

    @registro_email_duplicado
    Scenario: Tentar registrar usuário com email já existente
      Dado que existe um usuário pré-cadastrado com email "existinguser@example.com"
      E que existe um payload de registro com o mesmo email
      Quando eu envio uma requisição de registro
      Então a resposta deve retornar status 400
      E deve conter a mensagem "Já existe um usuário com este email."

    @registro_senhas_nao_coincidem
    Scenario: Tentar registrar usuário com senhas diferentes
      Dado que existe um payload de registro com senhas diferentes
      Quando eu envio uma requisição de registro
      Então a resposta deve retornar status 400
      E deve conter a mensagem "As senhas não coincidem."

    @registro_senha_fraca
    Scenario Outline: Tentar registrar usuário com senha fraca
      Dado que existe um payload de registro com a senha "<senha>"
      Quando eu envio uma requisição de registro
      Então a resposta deve retornar status 400
      E deve conter a mensagem "<mensagem>"

      Examples:
        | senha         | mensagem                                      |
        | abcdefgh      | Deve conter pelo menos 1 caractere maiúsculo. |
        | ABCDEFGH      | Deve conter pelo menos 1 dígito.              |
        | Abcdefgh      | Deve conter pelo menos 1 caractere especial.  |