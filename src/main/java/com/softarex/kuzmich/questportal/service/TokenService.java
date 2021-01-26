package com.softarex.kuzmich.questportal.service;

import com.softarex.kuzmich.questportal.entity.Token;
import com.softarex.kuzmich.questportal.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private RedisRepository redisRepository;

    @Autowired
    public TokenService(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    public Token createActiveToken(String token) {
        Token newToken = new Token(token, Token.Status.ACTIVE);
        redisRepository.save(newToken);
        return newToken;
    }
}
