package com.study.board.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity //@Entity는 이 클래스가 db에 있는 table을 의미한다는 어노테이션
// table 이름과 동일하게 해주는게 구분하기 쉬움
@Data //boardwritepro에 데이터를 주기위한 어노테이션
public class Board {
    // DB Table의 내용이 변경될 시 entity도 수정해주어야함

    @Id // primary key를 의미하는 어노테이션
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 전략 = 타입.아이덴티티(mariadb 전용)
    private Integer id;
    private String title;
    private String content;
    private String filename;
    private String filepath;

}


