package com.softarex.kuzmich.questportal.repository;

import com.softarex.kuzmich.questportal.entity.Response;
import com.softarex.kuzmich.questportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {
    List<Response> findAllByWorksheetId(int id);

    List<Response> findAllByUserId(User user);

    @Query("select MAX(worksheetId)  from Response")
    Integer findMaxWorksheetId();
}
