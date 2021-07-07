package com.kh.spring.board.model.service;

import java.util.ArrayList;

import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.Reply;
import com.kh.spring.common.model.vo.PageInfo;

public interface BoardService {

	// 게시판 리스트 조회 (페이징 처리)
	int selectListCount();
	ArrayList<Board> selectList(PageInfo pi);
	
	// 게시판 작성
	int insertBoard(Board b);
	
	// 게시판 상세조회
	int increaseCount(int boardNo);
	Board selectBoard(int boardNo);
	
	// 게시판 수정
	int updateBoard(Board b);
	
	// 게시판 삭제
	int deleteBoard(int boardNo);
	
	// 해당 게시글에 딸린 댓글 리스트 조회
	ArrayList<Reply> selectReplyList(int boardNo);
	
	// 댓글 작성
	int insertReply(Reply r);
}
