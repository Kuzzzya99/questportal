package com.softarex.kuzmich.questportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {

    private Integer id;
    private String firstName;
    private String lastName;
    private boolean raisedHand;
    private String role;
    private Integer userId;
}
