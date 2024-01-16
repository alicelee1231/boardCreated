package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;


@Slf4j
@Service
public class BoardService {

    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);

    @Autowired
    private BoardRepository boardRepository;

    //글작성
    public void write(Board board, MultipartFile file) throws Exception{

        try {
            String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files"; //저장경로지정
            System.out.println(projectPath+"여기");
            UUID uuid = UUID.randomUUID(); //랜덤 UUID 생성
            System.out.println(uuid +"여기");
            String fileName = uuid + "_" + file.getOriginalFilename(); //랜덤이름 저장
            System.out.println(file+"여기");
            File saveFile = new File(projectPath, fileName);

            file.transferTo(saveFile);

            board.setFilename(fileName);
            board.setFilepath("/files/" + fileName);
            boardRepository.save(board);
        }catch (Exception e){
            logger.error("error occured", e);
        }
    }
    //게시글 리스트 처리
    public Page<Board> boardList(Pageable pageable){
        return boardRepository.findAll(pageable);
    }


    public Page<Board>boardSearchList(String searchKeyword, Pageable pageable){
        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }

    //특정 게시글 불러오기
    public Board boardView(Integer id){
        return boardRepository.findById(id).get();
    }

    //특정 게시글 삭제
    public void boardDelete(Integer id){
        boardRepository.deleteById(id);
    }
}
