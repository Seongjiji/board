package com.study.board.repository;

import com.study.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> { //Board의 정보 저장, primary key의 타입

    // Repository에 method를 추가했기 때문에 Service부분에도 method를 추가해줘야함
    // → Service부분을 수정해주면 Controller 부분도 수정해줘야함
    Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable);

}
