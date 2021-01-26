package com.softarex.kuzmich.questportal.repository;

import com.softarex.kuzmich.questportal.entity.Field;
import com.softarex.kuzmich.questportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {

    Optional<Field> findById(int id);

    void deleteById(int id);

    List<Field> findAllByUser(final User user);

    Field findByLabel(String label);

// Optional<Field> findByLabel(Integer id, String login);
}
