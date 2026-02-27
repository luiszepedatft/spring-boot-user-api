package com.example.userapi.repository;

import com.example.userapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer > {
    @Query("SELECT DISTINCT u.email FROM User u WHERE u.email IS NOT NULL ORDER BY u.email")
    List<String> findAllEmails();
}
