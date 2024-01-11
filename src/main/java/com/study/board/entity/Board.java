package com.study.board.entity;


import jakarta.persistence.Entity;

@Entity
public class Board {

    private Integer id;
    private String title;
    private String content;
}
