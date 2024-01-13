package com.study.board.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String content;

    private String filename;
    private String filepath;
}
