@auth
Feature: Autenticação do Usuário
  Como um usuário do sistema
  Eu quero fazer login com meu email e senha
  Para poder acessar áreas protegidas

  Background:
    Given que eu limpei o banco de dados para garantir um estado inicial para os testes de login

  @login_sucesso
  Scenario: Login bem sucedido
    Given que existe um usuário pré-cadastrado com email "user@example.com" e senha "SenhaForte123!"
    And que existe um payload de login válido
    When eu envio uma requisição de login
    Then o status da resposta da API de login deve ser 200   
    And deve retornar o email do usuário e seus tokens

  @login_email_invalido
  Scenario: Tentar login com email inexistente
    Given que existe um payload de login com email "inexistente@example.com" e senha "SenhaForte123!"
    When eu envio uma requisição de login
    Then o status da resposta da API de login deve ser 400

  @login_senha_incorreta
  Scenario: Tentar login com senha incorreta
    Given que existe um usuário pré-cadastrado com email "user2@example.com" e senha "SenhaForte123!"
    And que existe um payload de login com email "user2@example.com" e senha "SenhaErrada456!"
    When eu envio uma requisição de login
    Then o status da resposta da API de login deve ser 400