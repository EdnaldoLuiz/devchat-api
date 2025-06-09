package com.ednaldoluiz.websocket.infra.persistence.repository;

import java.util.List;
import java.util.Optional;

import com.ednaldoluiz.websocket.web.controller.v1.chat.UserSummary;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ednaldoluiz.websocket.app.v1.user.dto.response.UserSearchResponse;
import com.ednaldoluiz.websocket.domain.model.user.User;

@Repository
public interface UserRepository extends BaseRepository<User> {

    Optional<User> findByEmail(String email);

    User getReferenceByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    @Query("""
        select new com.ednaldoluiz.websocket.app.v1.user.dto.response.UserSearchResponse(
            u.id, u.name, u.avatar, u.email
        )
        from User u
        where (lower(u.name) like lower(concat('%', :query, '%'))
           or lower(u.email) like lower(concat('%', :query, '%')))
          and u.deleted = false
        order by u.name
    """)
    List<UserSearchResponse> searchUsers(String query, Pageable pageable);

    @Query("""
      select new com.ednaldoluiz.websocket.web.controller.v1.chat.UserSummary(
        u.id, u.email, u.name, u.avatar
      )
      from User u
      where u.deleted = false
    """)
    List<UserSummary> findAllProjectedBy();
    
}
