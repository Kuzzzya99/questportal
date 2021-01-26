package com.softarex.kuzmich.questportal.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
public class ResponseKey implements Serializable {

    static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private int worksheetId;

}