package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import com.study.board.service.BoardService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;


@Controller
@Slf4j
public class BoardController  {

    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);

    @GetMapping("/log")
    public void log(){
        log.info("trace message");
        log.debug("debug message");
        log.error("error message");
        log.trace("trace message");
        log.warn("warn message");
    }


    @Autowired
    private BoardService  boardService;

    @Autowired
    private BoardRepository boardRepository;

    @GetMapping("/board/write")
    public String boardWriteForm( Board board, Model model){

        return "boardwrite";
    }

//    @PostMapping("/board/writepro")
//    public String boardWritePro(Board board, Model model, MultipartFile file) throws Exception{
//
//        boardService.write(board,file);
//
//        model.addAttribute("message","글 작성이 완료되었습니다.");
//        model.addAttribute("searchUrl","/board/list");
//
//        return "message";
//    }

    @GetMapping("/board/list")
    public String boardList(Model model,
                            @PageableDefault(page = 0, size =10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            String searchKeyword){

        Page<Board> list = null;
            //검색키워드가 안들어왔을 때
        if(searchKeyword == null){
            list = boardService.boardList(pageable);
        }else{
            list = boardService.boardSearchList(searchKeyword, pageable);
        }
//
        int nowPage = list.getPageable().getPageNumber()+1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("list",list);
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "boardList";
    }

    @GetMapping("/board/view")
    public String boardView(Model model,Integer id){
        model.addAttribute("board",boardService.boardView(id));
        return "boardview";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Integer id){
        boardService.boardDelete(id);
        return "redirect:/board/list";
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id, Model model) {

        try {
            model.addAttribute("board", boardService.boardView(id));
        } catch (Exception e) {
            logger.error("modify error");
        } return "boardmodify";
    }

    @GetMapping("/board/modifyFile/{id}")
    public String fileModify(@PathVariable("id")  Integer id, Model model, MultipartFile file) throws Exception {
        model.addAttribute("message", "업로드 파일 수정 완료했습니다.");
        return "message";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate( @PathVariable("id") Integer id, Board board, Model model, MultipartFile file) throws Exception{

        model.addAttribute("message", "글 수정이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");
        boardRepository.save(board);
        return "message";
    }
    @PostMapping("/board/save")
    public String boardSave(HttpServletRequest request, Board board, Model model) throws Exception{
        System.out.println(board.getTitle());

        if(board.getTitle().isEmpty()){
            System.out.println();
            request.setAttribute("message","타이틀을 입력해 주세요");
            model.addAttribute("searchUrl", "/board/write");

            return "message";
        }else if(board.getContent().isEmpty()) {
            request.setAttribute("message","컨텐츠를 입력해주세요");
            model.addAttribute("searchUrl", "/board/write");

            return "message";
        }
        model.addAttribute("message", "글 등록이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");
        boardRepository.save(board);
        return "message";
    }
}
