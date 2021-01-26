package com.softarex.kuzmich.questportal.repository;

import com.softarex.kuzmich.questportal.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeRepository extends JpaRepository<Type, Integer> {
    Optional<Type> findById(Integer id);
}
