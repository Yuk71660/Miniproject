package com.miniproject.service.board;

import java.util.List;

import com.miniproject.model.BoardDetailInfo;
import com.miniproject.model.BoardUpFilesVODTO;
import com.miniproject.model.HBoard;
import com.miniproject.model.HBoardDTO;

public interface HBoardService {
	List<HBoard> getEntireHBoard() throws Exception;

	boolean saveBoard(HBoardDTO newBoard, List<BoardUpFilesVODTO> fileList) throws Exception;
	
	BoardDetailInfo getBoardDetailInfo(int boardNo, String ip) throws Exception;

	boolean saveReply(HBoardDTO newReply) throws Exception;
}
