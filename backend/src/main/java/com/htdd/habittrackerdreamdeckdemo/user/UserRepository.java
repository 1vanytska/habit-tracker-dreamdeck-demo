package com.htdd.habittrackerdreamdeckdemo.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email); // Для входу
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}