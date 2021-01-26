package com.softarex.kuzmich.questportal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
@Table(name = "fields")
public class Field implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "required", nullable = false)
    private boolean required;

    @Column(name = "active", nullable = false)
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private Type type;

    @OneToMany(mappedBy = "field", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Option> options;

    @OneToMany(mappedBy = "fieldId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Response> responses;

    public Field() {
    }

    @Override
    public String toString() {
        return "Field{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", required=" + required +
                ", active=" + active +
                ", type=" + type +
                '}';
    }
}
