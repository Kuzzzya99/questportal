package com.softarex.kuzmich.questportal.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResponseDTO {
    private List<AnswerDTO> response;

    public List<AnswerDTO> getResponse() {
        return response;
    }

    public void setResponse(List<AnswerDTO> response) {
        this.response = response;
    }

    public ResponseDTO(List<AnswerDTO> response) {
        this.response = response;
    }

    public ResponseDTO() {
    }


}
