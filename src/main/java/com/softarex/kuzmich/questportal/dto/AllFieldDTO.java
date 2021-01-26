package com.softarex.kuzmich.questportal.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AllFieldDTO {
    private int fieldId;
    private String label;
    private int type;
    private boolean required;
    private boolean active;

    public AllFieldDTO(int fieldId, String label, int type, boolean required, boolean active) {
        this.fieldId = fieldId;
        this.label = label;
        this.type = type;
        this.required = required;
        this.active = active;
    }
}
