package com.softarex.kuzmich.questportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCommentDTO {
    private String username;
    private String comment;
}