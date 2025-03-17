@auth
Feature: Recuperação de Senha
  Como um usuário do sistema
  Eu quero solicitar um reset de senha
  Para redefinir minha senha caso eu esqueça

  Background:
    Dado que eu limpei o banco de dados para garantir um estado inicial

  @reset_valido
  Scenario: Solicitar reset de senha com email cadastrado
    Dado que existe um usuário pré-cadastrado com email "exemplo@teste.com" e senha "SenhaForte123!"
    E que existe um payload de recuperação para o email "exemplo@teste.com"
    Quando eu envio uma requisição de redefinição de senha
    Então a resposta deve ser de sucesso com status 200
    E deve conter a mensagem "Se o e-mail existir, uma mensagem de redefinição foi enviada."

  @reset_email_invalido
  Scenario: Solicitar reset de senha com email inexistente
    Dado que existe um payload de recuperação para o email "inexistente@teste.com"
    Quando eu envio uma requisição de redefinição de senha
    Então a resposta deve retornar status 400
    E deve conter a mensagem "O e-mail informado não está cadastrado."