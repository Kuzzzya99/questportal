package com.softarex.kuzmich.questportal.dto;

import lombok.Data;

@Data
public class FieldDTO {

    private int id;
    private String label;
    private int type;
    public String[] options;
    private boolean required;
    private boolean active;
    private int userId;

}
