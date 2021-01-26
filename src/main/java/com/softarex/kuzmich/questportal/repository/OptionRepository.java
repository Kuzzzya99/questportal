package com.softarex.kuzmich.questportal.repository;

import com.softarex.kuzmich.questportal.entity.Field;
import com.softarex.kuzmich.questportal.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionRepository extends JpaRepository<Option, Integer> {
    Optional<Option> findById(Integer id);

    List<Option> findAllByField(final Field field);

    List<Option> deleteAllByField(final Field field);
}
