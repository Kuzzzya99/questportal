package com.softarex.kuzmich.questportal.repository;

import com.softarex.kuzmich.questportal.entity.Comment;
import com.softarex.kuzmich.questportal.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByFile (File file);
}
