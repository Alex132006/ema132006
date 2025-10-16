package com.ema.api.repository;

import com.ema.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRoleAndStatus(String role, String status);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
}
