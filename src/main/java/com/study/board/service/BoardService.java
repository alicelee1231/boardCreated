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
import org.springframework.util.ObjectUtils;
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
            UUID uuid = UUID.randomUUID(); //랜덤 UUID 생성
            String fileName = uuid + "_" + file.getOriginalFilename(); //랜덤이름 저장
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

    //게시글 업데이트
    public String boardUpdate(Integer id, Board board, MultipartFile file, String filename, String filePath) throws Exception {

        Board boardTemp = this.boardView(id);

        //새로운 타이틀 및 내용으로 변경
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        //새로운 파일 패쓰 저장
        boardTemp.setFilepath(board.getFilepath());
        boardTemp.setFilename(board.getFilename());

        if(ObjectUtils.isEmpty(file)){
            String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files"; //저장경로지정
            UUID uuid = UUID.randomUUID(); //랜덤 UUID 생성
            String fileName = uuid + "_" + file.getOriginalFilename(); //랜덤이름 저장
            File saveFile = new File(projectPath, fileName);

            file.transferTo(saveFile);

            board.setFilename(fileName);
            board.setFilepath("/files/" + fileName);
            boardRepository.save(board);
        }else{
            this.write(boardTemp, file);
        }

        return "message";
    }


}
