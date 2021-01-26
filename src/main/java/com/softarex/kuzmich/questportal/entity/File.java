package com.softarex.kuzmich.questportal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "rate")
    private int rate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "can_watch_id", referencedColumnName = "id")
    private List<User> canWatch;

    @OneToMany(mappedBy = "file", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments;

    @Override
    public String toString() {
        return "File{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rate=" + rate +
                ", canWatch=" + canWatch +
                '}';
    }
}
