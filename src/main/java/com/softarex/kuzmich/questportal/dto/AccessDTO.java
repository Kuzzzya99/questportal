package com.softarex.kuzmich.questportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessDTO {
    private List<Integer> userIds;
    private Integer fileId;
}
