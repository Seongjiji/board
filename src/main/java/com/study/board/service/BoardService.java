package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {


  @Autowired // = new BoardRepository();의 역할을 이 어노테이션이 해줌
    private BoardRepository boardRepository;

    // 게시글 작성 처리 ++ 파일 업로드(MultipartFile 추가)
    // ... 이걸 추가하주면 컨트롤러 부분에서 이걸 사용하는 부분이 있기 때문에 오류 발생 → 추가로 컨트롤러 부분에서도 수정 필요
    public void write(Board board, MultipartFile file) throws Exception{

        // 1. 저장할 경로 지정 : 프로젝트 경로를 projectPath에 담아줌
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
        //식별자 : 담기는 이름을 랜덤으로 생성
        UUID uuid = UUID.randomUUID();
        // 식별자를 이용해 저장될 filename 설정
        String fileName = uuid + "_" + file.getOriginalFilename();
        // 2. 파일이 저장될 장소 설정 : 경로와 이름 지정 = new File(파일경로, 담기는 이름)
        File saveFile = new File(projectPath, fileName);
        // 3. 파일 저장
        file.transferTo(saveFile);

        // + Table에 파일 이름과 파일 경로 저장하기
        board.setFilename(fileName);
        board.setFilepath("/files/" + fileName);

        boardRepository.save(board);
    }


    // 게시글 리스트 처리
    // ++ 페이징 처리를 위해 Pageable 추가
    // public List<Board> boardlist(Pageable pageable) { // 글을 블러올 메소드
    public Page<Board> boardlist(Pageable pageable) { // 기존 매개변수가 없는 경우엔 return값이 List로 넘어오는데 이 경우 return값이 Page로 넘어옴
        // ↓ 여기도 pageable 추가 : 이때 기존 List<Board>사용시 페이저블을 추가하게 되면 오류가 ↑여기에 오류가 발생하기 때문에 Page<board>로 수정
        return boardRepository.findAll(pageable);
    }

    // + 게시글 검색
    public Page<Board> boarkSearchList(String searchKeyword, Pageable pageable){
        return boardRepository.findByTitleContaining(searchKeyword, pageable); // service부분을 수정해줬으니 controller 부분도 수정해줘야함

    }


    // 게시글 불러오기
    public Board boardView(Integer id) {
        return boardRepository.findById(id).get();

    }

    // 게시글 삭제 (void는 return 타입이 없음)
    public void boardDelete(Integer id) {
        boardRepository.deleteById(id);

    }





}
