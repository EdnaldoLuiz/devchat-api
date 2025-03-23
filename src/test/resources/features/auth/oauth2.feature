@auth
@Disabled
Feature: Login via OAuth2
  Como um usuário do sistema
  Eu quero fazer login usando um provedor OAuth2
  Para que eu possa acessar a aplicação sem precisar de email e senha locais

  @oauth2_registro @positive
  Scenario Outline: Registro via OAuth2 (criação de conta) com validação
    Given que possuo atributos válidos do "<provider>"
    And que vou usar o provider "<provider>"
    And o usuário não está cadastrado no sistema
    When eu envio uma requisição de login OAuth2
    Then o status da resposta da API de OAuth2 deve ser 200
    And deve conter a mensagem de OAuth2 "Login via <provider> com sucesso!"
    And deve conter um token de acesso
    And o usuário "<email>" agora existe no banco de dados
    And a senha do usuário "<email>" é igual a "<password>"

    Examples:
      | provider | email                     | password |
      | GITHUB   | githubuser@example.com    | ""       |
      | GOOGLE   | googleuser@example.com    | ""       |

  @oauth2_login_atributos_invalidos @negative
  Scenario Outline: Tentar login via OAuth2 com atributo obrigatório faltando
    Given que possuo atributos inválidos do "<provider>" com o campo "<campo>" ausente
    And que vou usar o provider "<provider>"
    When eu envio uma requisição de login OAuth2
    Then o status da resposta da API de OAuth2 deve ser 400
    And deve conter a mensagem de OAuth2 "<mensagem>"

    Examples:
      | provider | campo | mensagem                                      |
      | GITHUB   | email | GitHub OAuth2: Email é obrigatório.           |
      | GITHUB   | login | GitHub OAuth2: Login é obrigatório.           |
      | GOOGLE   | email | Google OAuth2: Email é obrigatório.           |
      | GOOGLE   | name  | Google OAuth2: Nome é obrigatório.            |

  @oauth2_login_sucesso @positive
  Scenario Outline: Login via OAuth2 com atributos válidos
    Given que possuo atributos válidos do "<provider>"
    And que vou usar o provider "<provider>"
    When eu envio uma requisição de login OAuth2
    Then o status da resposta da API de OAuth2 deve ser 200
    And deve conter a mensagem de OAuth2 "Login via <provider> com sucesso!"
    And deve conter um token de acesso

    Examples:
      | provider |
      | GITHUB   |
      | GOOGLE   |