package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.SQLOutput;

@Controller
public class BoardController {

    @Autowired // 해당 페이지에서 boardService.write가 무엇인지 알 수 있도록 설정
    private BoardService boardService;

    @GetMapping("/board/write") // localhost:8080/board/write가 입력되면 "boardwrtie"로 이동
    public String boardWriteForm() {
        return "boardwrite";
    }

    // 글 작성
    @PostMapping("/board/writepro")
    public String boardWritePro(Board board, Model model, MultipartFile file) throws Exception {

        boardService.write(board, file);

        // 메세지 출력을 위해  Model↑ 추가해주고 ↓attr도 추가가
        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        //return "redirect:/board/list";
        return "message";

    }
    /* 이런식으로도 만들어도 되지만 받아오는 값이 많아질 수 있으니 위처럼
    public String boardWritePro(String title, String content){
        System.out.println("글 제목 : "+title);
        System.out.println("글 내용 : "+content);

        return "";
    }*/

    // 페이징 처리를 위해선 이 부분에서 수정 : 어노테이션 PageableDefault와 Pageable 인터페이스 사용
                                        // Model : 데이터를 담아서 우리가 보는 페이지로 보내주는 역할
    @GetMapping("/board/list")          // (page=디폴트 페이지, size=한 페이지 게시글 수, sort=정렬기준, direction=정렬순서(DESC:역순))
    public String boardList(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            String searchKeyword) {

        // ++검색기능 추가 : searchKeyword가 들어왔을 때와 들어오지않았을 때 구분 (검색 했을 떄와 검색하지 않았을 때를 구별해줘야하기 때문에 나눠주도록 함)
        Page<Board> list = null; //list 선언
        if(searchKeyword == null){
            list = boardService.boardlist(pageable);
        }else{
            list = boardService.boarkSearchList(searchKeyword, pageable);
        }

        /* 프론트엔드에서 리스트 처리를 하기 위해 이 부분을 아래와 같이 수정
        // 페이징을 위해 보드리스트()에 pageable 추가
        model.addAttribute("list", boardService.boardlist(pageable));
         */
        // Page<Board> list = boardService.boardlist(pageable); 검색기능 추가하면서 이 부분 위(if문 부분)로 이동

        // nowpage = list.getpageable.getpagenumber : 리스트 페이져블에서 넘어온 현재 페이지를 가지고 올 수 있음
        int nowPage = list.getPageable().getPageNumber() + 1; // 시작점이 0이기 때문에 우리가 보는 페이지에서 시작점을 1로 하기 위해선 +1을 해주어야함
        int startPage = Math.max(nowPage - 4, 1); //nowpage가 1일 경우 -3이 되므로 그걸 방지하기 위해 Math.max(x, y)로 설정하여 1보다 작게는 나올 수 없게함
        int endPage = Math.min(nowPage + 5, list.getTotalPages()); //위에 동일하게 마지막 페이지가 최소 토탈페이지를 넘을 수 없게 설정함

        // 타임리프에서 출력하기 위해 변수들을 넘겨줌
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("list", list);

        return "boardlist";

    }

    @GetMapping("/board/view")
    public String boardView(Model model, Integer id){ // 넘겨 받을 때는 매개변수로 Model을 써야함
        model.addAttribute("board", boardService.boardView(id));
        System.out.println();
        return "boardview";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Integer id, Model model) {
        boardService.boardDelete(id);

        model.addAttribute("message", "글이 삭제되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        //return "redirect:/board/list";
        return "message";
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id,
                              Model model) {
                            //PathVariable이 {id}를 인식해서 Integer 형태의 아이디로 받음
        model.addAttribute("board", boardService.boardView(id));
        return "boardmodify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Board board, Model model, MultipartFile file) throws Exception{
        //기존에 있던 글이 담겨져서 오는 객체 생성
        Board boardTemp = boardService.boardView(id);
        //수정
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        boardService.write(boardTemp, file);

        model.addAttribute("message", "글이 수정되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        //return "redirect:/board/list";
        return "message";
    }

}
