package com.brainstation23.skeleton.data.repository.user;

import com.brainstation23.skeleton.data.entity.user.Users;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
    Optional<Users> findByEmail(String email);
    Optional<Users> findByUsernameAndEmail(String username, String email);

    @Query("SELECT u FROM Users u WHERE u.username LIKE %:usernamePattern%")
    List<Users> findUsersByUsernameLike(@Param("usernamePattern") String usernamePattern, Pageable pageable);

    Optional<Users> findByUserIdentity(String userIdentity);
}
