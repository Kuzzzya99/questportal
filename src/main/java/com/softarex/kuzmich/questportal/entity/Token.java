package com.softarex.kuzmich.questportal.entity;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.UUID;

@Data
@RedisHash("Token")
public class Token implements Serializable {

    public Token(String token, Status status) {
        this.id = UUID.randomUUID().toString();
        this.token = token;
        this.status = status;
    }

    public enum Status {
        ACTIVE, INVALID
    }

    @Id
    private String id;

    @Indexed
    private String token;
    private Status status;

}
