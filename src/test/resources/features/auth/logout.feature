@auth
Feature: Logout
  Como um usuário do sistema
  Eu quero realizar logout
  Para que minha sessão seja encerrada e meu token invalidado

  @positive
  Scenario: Logout com sucesso
    When eu envio uma requisição de logout
    Then o status da resposta da API de logout deve ser 200
    And deve conter a mensagem de logout "Logout realizado com sucesso."