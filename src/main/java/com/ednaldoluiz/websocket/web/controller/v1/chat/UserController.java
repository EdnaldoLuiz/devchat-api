package com.ednaldoluiz.websocket.web.controller.v1.chat;

import com.ednaldoluiz.websocket.infra.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository repo;

    @GetMapping
    public List<UserSummary> list() {
        return repo.findAllProjectedBy();
    }
}
