package com.study.board.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name="board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @NotEmpty
    private String title;

    @NotNull
    @NotEmpty
    private String content;

    private String filename;
    private String filepath;
}
