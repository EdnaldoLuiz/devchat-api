package com.ednaldoluiz.websocket.infra.persistence;

import java.util.List;
import java.util.Optional;

import com.ednaldoluiz.websocket.web.controller.v1.chat.UserSummary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ednaldoluiz.websocket.domain.model.user.User;

@Repository
public interface UserRepository extends BaseRepository<User> {

    Optional<User> findByEmail(String email);

    User getReferenceByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    @Query("""
      select new com.ednaldoluiz.websocket.web.controller.v1.chat.UserSummary(
        u.id, u.email, u.name, u.avatar
      )
      from User u
      where u.deleted = false
    """)
    List<UserSummary> findAllProjectedBy();
    
}
