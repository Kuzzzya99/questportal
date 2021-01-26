package com.softarex.kuzmich.questportal.dto;

import lombok.Data;

@Data
public class AnswerDTO {
    private int id;
    private String label;
    private String answer;

    public AnswerDTO(int id, String label, String answer) {
        this.id = id;
        this.label = label;
        this.answer = answer;
    }

    public AnswerDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
