package com.playmate.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(length = 10)
    private String icon = "";

    @Column
    private Integer sort = 0;

    @Column(columnDefinition = "BOOLEAN DEFAULT 1")
    private Boolean status = true;
}
