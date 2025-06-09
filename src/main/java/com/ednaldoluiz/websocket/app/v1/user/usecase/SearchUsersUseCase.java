package com.ednaldoluiz.websocket.app.v1.user.usecase;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.ednaldoluiz.websocket.app.v1.user.dto.response.UserSearchResponse;
import com.ednaldoluiz.websocket.infra.persistence.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchUsersUseCase {

    private final UserRepository repo;

    public List<UserSearchResponse> execute(String query, int limit) {
        log.info("Buscando usuários por '{}'", query);
        int max = Math.min(limit, 50); // hard cap
        return repo.searchUsers(query, PageRequest.of(0, max));
    }
}
