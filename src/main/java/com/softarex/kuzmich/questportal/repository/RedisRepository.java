package com.softarex.kuzmich.questportal.repository;


import com.softarex.kuzmich.questportal.entity.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisRepository extends CrudRepository<Token, String> {
    Token findByToken(String token);
}
