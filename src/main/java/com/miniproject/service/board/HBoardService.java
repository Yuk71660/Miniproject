package com.miniproject.service.board;

import java.util.List;

import com.miniproject.model.BoardDetailInfo;
import com.miniproject.model.BoardUpFilesVODTO;
import com.miniproject.model.HBoard;
import com.miniproject.model.HBoardDTO;

public interface HBoardService {
	// 전체 게시글 리스트 얻어오는
	List<HBoard> getEntireHBoard() throws Exception;

	// 게시글 저장
	boolean saveBoard(HBoardDTO newBoard, List<BoardUpFilesVODTO> fileList) throws Exception;

	// 게시글 상세조회
	BoardDetailInfo getBoardDetailInfo(int boardNo, String ipAddr) throws Exception;

	// 답글 저장
	boolean saveReply(HBoardDTO newReply) throws Exception;

	boolean modifyBoard(HBoardDTO modifyBoard, List<BoardUpFilesVODTO> modifyFileList) throws Exception;

	boolean removeBoardProcess(int boardNo) throws Exception;
}
