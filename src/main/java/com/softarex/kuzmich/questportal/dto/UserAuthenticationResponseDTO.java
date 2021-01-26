package com.softarex.kuzmich.questportal.dto;

import lombok.Data;

@Data
public class UserAuthenticationResponseDTO {
    private String tokenId;
    private String token;
    private int id;
}
