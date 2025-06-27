package com.ednaldoluiz.websocket.infra.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.ednaldoluiz.websocket.web.controller.v1.chat.UserSummary;

import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.ednaldoluiz.websocket.app.v1.user.dto.response.UserSearchResponse;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.infra.persistence.projection.NameAvatarProjection;

@Repository
public interface UserRepository extends BaseRepository<User> {

    Optional<User> findByEmail(String email);

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
                select u.id
                from User u
                where u.deleted = false
                order by u.id
            """)
    @QueryHints({
            @QueryHint(name = "org.hibernate.readOnly", value = "true"),
            @QueryHint(name = "org.hibernate.fetchSize", value = "5000")
    })
    Stream<Long> streamAllActiveIds();

    @QueryHints({
            @QueryHint(name = "org.hibernate.readOnly", value = "true"),
    })
    @Query("""
                SELECT new com.ednaldoluiz.websocket.infra.persistence.projection.NameAvatarProjection(
                    u.id, u.name, u.avatar
                )
                FROM User u
                WHERE u.id = :id
            """)
    Optional<NameAvatarProjection> findNameAndAvatarById(Long id);

    @Query("""
              select new com.ednaldoluiz.websocket.web.controller.v1.chat.UserSummary(
                u.id, u.email, u.name, u.avatar
              )
              from User u
              where u.deleted = false
            """)
    List<UserSummary> findAllProjectedBy();

}
