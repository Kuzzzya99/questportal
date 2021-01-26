package com.softarex.kuzmich.questportal.dto;

import com.softarex.kuzmich.questportal.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDTO {
    private Integer id;
    private String name;
    private int rate;
    private Integer owner;
    private List<Integer> canWatch;
}
