// src/main/java/com/ednaldoluiz/websocket/app/v1/user/usecase/SearchUsersUseCase.java
package com.ednaldoluiz.websocket.app.v1.user.usecase;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ednaldoluiz.websocket.app.v1.user.dto.request.SearchUsersRequest;
import com.ednaldoluiz.websocket.app.v1.user.dto.response.UserSearchResponse;
import com.ednaldoluiz.websocket.infra.persistence.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchUsersUseCase {

    private static final int HARD_CAP = 50;
    private final UserRepository repo;

    /**
     * Executa a busca de usuários, respeitando cap de 50 registros.
     */
    public List<UserSearchResponse> execute(SearchUsersRequest req) {
        log.info("Buscando usuários por '{}'", req.query());
        int pageSize = HARD_CAP;
        return repo.searchUsers(req.query(), PageRequest.of(0, pageSize));
    }
}
