package com.softarex.kuzmich.questportal.repository;

import com.softarex.kuzmich.questportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);

    Optional<User> findById(int id);

    boolean existsByLogin(String login);

    Optional<User> findByIdAndLogin(Integer id, String login);
}