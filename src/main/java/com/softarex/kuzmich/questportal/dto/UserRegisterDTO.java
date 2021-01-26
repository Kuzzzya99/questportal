package com.softarex.kuzmich.questportal.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserRegisterDTO {
    @NotBlank
    @Size(min = 5, max = 30)
    private String login;

    @NotBlank
    @Size(min = 5, max = 30)
    private String password;

    @NotBlank
    @Size(min = 2, max = 20)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 20)
    private String lastName;

    @NotBlank
    @Size(min = 5, max = 30)
    private String phoneNumber;
}
