package com.softarex.kuzmich.questportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Integer userId;
    private String comment;
    private Integer fileId;
}
