package com.softarex.kuzmich.questportal.entity;

import com.softarex.kuzmich.questportal.dto.AnswerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "responses")
@IdClass(ResponseKey.class)
public class Response implements Serializable {
    @Id
    @Column(name = "id")
    private int id;

    @Id
    @Column(name = "worksheet_id")
    private int worksheetId;

    @Column(name = "answer", nullable = false)
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id", referencedColumnName = "id")
    private Field fieldId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userId;

    public AnswerDTO toDto() {
        return new AnswerDTO(id, fieldId.getLabel(), answer);
    }

}

