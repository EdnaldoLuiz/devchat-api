// src/main/java/com/ednaldoluiz/websocket/infra/schedule/SignalKeyScheduler.java
package com.ednaldoluiz.websocket.infra.schedule;

import com.ednaldoluiz.websocket.infra.persistence.repository.UserRepository;
import com.ednaldoluiz.websocket.infra.persistence.repository.UserSignalKeysRepository;
import com.ednaldoluiz.websocket.shared.config.SignalKeyProperties;
import com.ednaldoluiz.websocket.web.websocket.event.SignalEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignalKeyJob {

    private final UserSignalKeysRepository keyRepository;
    private final UserRepository userRepository;
    private final SignalEventPublisher eventPublisher;
    private final SignalKeyProperties config;

    /**
     * Verifica chaves signedPreKey expiradas e notifica usuários.
     */
    @Transactional
    @Scheduled(cron = "${signal.check-spk-cron}")
    public void checkSignedPreKeys() {
        String correlationId = UUID.randomUUID().toString();
        log.info("[{}] Iniciando job de verificação de signedPreKeys expiradas", correlationId);

        try (var expiredUserIds = keyRepository.streamExpiredSignedPreKeyUserIds(config.signedPreKeyTtlDays(), config.batchSize())) {
            expiredUserIds.forEach(userId -> {
                try {
                    log.info("[{}] Notificando expiração da signedPreKey para userId={}", correlationId, userId);
                    eventPublisher.notifySignedPreKeyExpired(userId);
                } catch (Exception e) {
                    log.warn("[{}] Erro ao notificar expiração de signedPreKey para userId={}: {}", correlationId, userId, e.getMessage(), e);
                }
            });
        } catch (Exception ex) {
            log.error("[{}] Falha geral ao processar signedPreKeys expiradas", correlationId, ex);
        }
        log.info("[{}] Finalizado job de verificação de signedPreKeys", correlationId);
    }

    /**
     * Limpa bundles órfãos (usuário foi deletado, mas bundle ficou).
     */
    @Transactional(readOnly = true)
    @Scheduled(cron = "${signal.cleanup-orphans-cron}")
    public void cleanupOrphanBundles() {

        String cid = UUID.randomUUID().toString();
        log.info("[{}] Início clean-up bundles órfãos", cid);

        try (Stream<Long> validUserIds = userRepository.streamAllActiveIds();
             Stream<Long> bundleUserIds = keyRepository.streamAllUserIds(config.batchSize()) )
        {
            Iterator<Long> validIt = validUserIds.iterator();

            Set<Long> valid = new HashSet<>();
            validIt.forEachRemaining(valid::add);

            bundleUserIds
                    .filter(id -> !valid.contains(id))
                    .forEach(orphan -> {
                        try {
                            keyRepository.deleteByUserId(orphan);
                            log.info("[{}] Deleted orphan bundle userId={}", cid, orphan);
                        } catch (Exception e) {
                            log.warn("[{}] Falha delete orphan userId={}", cid, orphan, e);
                        }
                    });

        } catch (Exception e) {
            log.error("[{}] Erro geral clean-up bundles órfãos", cid, e);
        }

        log.info("[{}] Fim clean-up bundles órfãos", cid);
    }

}
