package com.ayman.bankapp.bankingapplication.repositories;

import com.ayman.bankapp.bankingapplication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsById(String id);
    boolean existsByEmail(String email);
    User findUserById(String id);
}
