package com.ednaldoluiz.websocket.web.controller.v1.signal;

/**
 * Documentação dos endpoints de gerenciamento de Signal Keys (E2EE).
 */
public interface SignalKeyDocs {

    /**
     * Documentação para o endpoint de upload (salvar/atualizar) do bundle de chaves Signal.
     */
    interface UploadBundle {
        String SUMMARY = "Salvar/Atualizar Bundle de Chaves Signal";
        String DESCRIPTION = """
            <html>
                <body>
                    <h3>Salva ou atualiza o bundle principal de chaves do usuário para comunicação E2EE (Signal Protocol).</h3>
                    <p>
                        <ul>
                            <li><b>Salva:</b> Identity key, signed pre-key e até 100 one-time pre-keys.</li>
                            <li><b>Sobrescreve</b> qualquer bundle anterior do usuário.</li>
                            <li>Recomenda-se enviar este bundle imediatamente após o registro ou rotação de chaves no dispositivo cliente.</li>
                        </ul>
                    </p>
                    <h4>Campos esperados no body:</h4>
                    <ul>
                        <li><code>registrationId</code> (int) — ID de registro Signal (único por dispositivo)</li>
                        <li><code>identityKey</code> (byte[]) — Chave pública de identidade do usuário</li>
                        <li><code>signedPreKeyId</code> (int) — ID da signed pre-key</li>
                        <li><code>signedPreKey</code> (byte[]) — Chave pública signed pre-key</li>
                        <li><code>signedPreKeySig</code> (byte[]) — Assinatura da signed pre-key</li>
                        <li><code>oneTimePreKeys</code> (array de objetos)
                            <ul>
                                <li><code>keyId</code> (int) — ID da one-time pre-key</li>
                                <li><code>publicKey</code> (byte[]) — Chave pública da one-time pre-key</li>
                            </ul>
                        </li>
                    </ul>
                </body>
            </html>
        """;

        String STATUS_200_RESPONSE = """
            {
                "message": "Key bundle salvo com sucesso"
            }
        """;

        String STATUS_400_RESPONSE = """
            {
                "timestamp": "2025-06-20T15:55:01",
                "status": 400,
                "error": "Request inválido ou mal formatado.",
                "message": "Campo 'signedPreKey' não pode ser nulo.",
                "path": "/api/v1/keys/bundle"
            }
        """;
    }

    /**
     * Documentação para o endpoint de busca do bundle de chaves Signal.
     */
    interface FetchBundle {
        String SUMMARY = "Buscar Bundle de Chaves Signal";
        String DESCRIPTION = """
            <html>
                <body>
                    <h3>Recupera o bundle de chaves Signal para o usuário autenticado.</h3>
                    <ul>
                        <li>Usado na primeira inicialização do cliente Signal/WebSocket, antes do início de uma sessão E2EE.</li>
                        <li>Se não houver bundle cadastrado, retorna 404.</li>
                    </ul>
                </body>
            </html>
        """;

        String STATUS_200_RESPONSE = """
            {
                "registrationId": 67890,
                "identityKey": "base64-encoded",
                "signedPreKeyId": 2002,
                "signedPreKey": "base64-encoded",
                "signedPreKeySig": "base64-encoded",
                "preKeys": [
                    {
                        "preKeyId": 888,
                        "preKey": "base64-encoded"
                    }
                ]
            }
        """;

        String STATUS_404_RESPONSE = """
            {
                "timestamp": "2025-06-20T15:55:01",
                "status": 404,
                "error": "Bundle não encontrado para o usuário.",
                "message": "Nenhum key bundle foi cadastrado ainda.",
                "path": "/api/v1/keys/bundle"
            }
        """;
    }

    interface FetchPreKeyBundle {
        String SUMMARY = "Buscar uma pre-key para handshake (consome atômico)";
        String DESCRIPTION = """
            <html>
              <body>
                <h3>Obtém um <b>bundle mínimo</b> para handshake com outro usuário (Signal Protocol).</h3>
                <ul>
                  <li>Retorna: <b>1</b> pre-key disponível + signedPreKey do alvo (targetId).</li>
                  <li>Já marca a pre-key como consumida na mesma transação (garante forward secrecy).</li>
                  <li>Se o estoque está baixo (≤ 20), o alvo recebe notificação push <code>LOW_PREKEY</code> via WebSocket.</li>
                  <li>Se não houver pre-keys disponíveis, retorna HTTP 410.</li>
                </ul>
                <p>Use sempre antes de iniciar uma sessão E2EE com outro usuário.</p>
              </body>
            </html>
        """;

        String STATUS_200_RESPONSE = """
            {
              "registrationId": 1234,
              "identityKey": "base64-encoded",
              "signedPreKeyId": 5678,
              "signedPreKey": "base64-encoded",
              "signedPreKeySig": "base64-encoded",
              "oneTimePreKeyId": 42,
              "oneTimePreKey": "base64-encoded"
            }
        """;

        String STATUS_410_RESPONSE = """
            {
              "timestamp": "2025-06-22T15:30:00",
              "status": 410,
              "error": "Out of pre-keys",
              "message": "O usuário não possui mais pre-keys disponíveis.",
              "path": "/api/v1/signal/keys/fetch/{targetId}"
            }
        """;

        String STATUS_404_RESPONSE = """
            {
              "timestamp": "2025-06-22T15:30:00",
              "status": 404,
              "error": "Bundle não encontrado para o usuário.",
              "message": "Nenhum key bundle foi cadastrado ainda.",
              "path": "/api/v1/signal/keys/fetch/{targetId}"
            }
        """;
    }

    /**
     * Documentação para o endpoint de rotação da signedPreKey.
     */
    interface RotateSPK {
        String SUMMARY = "Rotacionar a signedPreKey do device";
        String DESCRIPTION = """
            <html>
              <body>
                <h3>Atualiza a <b>signedPreKey</b> e sua assinatura para o usuário autenticado.</h3>
                <ul>
                  <li>Recomendado após receber evento <code>SPK_EXPIRED</code> do servidor.</li>
                  <li>Garante que novas sessões E2EE usem chaves frescas (segurança forward secrecy).</li>
                  <li>Assinatura (signedPreKeySig) deve ser feita com a identityKey privada.</li>
                </ul>
              </body>
            </html>
        """;

        String STATUS_200_RESPONSE = """
            {
              "message": "SignedPreKey rotacionada com sucesso"
            }
        """;

        String STATUS_400_RESPONSE = """
            {
              "timestamp": "2025-06-22T15:35:00",
              "status": 400,
              "error": "Request inválido",
              "message": "Chave ou assinatura mal formada.",
              "path": "/api/v1/signal/keys/rotate-spk"
            }
        """;
    }

    /**
     * Documentação para o endpoint de status do estoque de pre-keys.
     */
    interface Status {
        String SUMMARY = "Consultar estoque de pre-keys e validade da signedPreKey";
        String DESCRIPTION = """
            <html>
              <body>
                <h3>Consulta rápida sobre o <b>estoque de pre-keys</b> e validade da <b>signedPreKey</b> atual.</h3>
                <ul>
                  <li>Permite ao app decidir quando precisa reabastecer as pre-keys ou rotacionar a signedPreKey.</li>
                  <li>Recomendado consultar ao abrir a tela de configurações E2EE.</li>
                </ul>
              </body>
            </html>
        """;

        String STATUS_200_RESPONSE = """
            {
              "remainingPreKeys": 12,
              "spkValidUntil": "2025-07-31T03:00:00Z"
            }
        """;
    }
}
