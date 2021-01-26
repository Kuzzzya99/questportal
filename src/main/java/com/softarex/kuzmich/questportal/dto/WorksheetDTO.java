package com.softarex.kuzmich.questportal.dto;

import lombok.Data;

import java.util.List;

@Data
public class WorksheetDTO {
    private int worksheetId;
    private List<AnswerDTO> answerDTOS;
}
