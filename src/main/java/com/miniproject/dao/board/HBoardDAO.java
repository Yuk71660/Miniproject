package com.miniproject.dao.board;

import java.util.List;

import com.miniproject.model.BoardDetailInfo;
import com.miniproject.model.BoardUpFilesVODTO;
import com.miniproject.model.HBoard;
import com.miniproject.model.HBoardDTO;

public interface HBoardDAO {

	List<HBoard> selectAllHBoard() throws Exception;

	int insertHBoard(HBoardDTO newBoard) throws Exception;

	int insertHBoardUpfile(BoardUpFilesVODTO upFile) throws Exception;

	BoardDetailInfo selectBoardDetailInfo(int boardNo) throws Exception;

	int checkHourReadLogByBoardNo(String readWho, int boardNo) throws Exception;

	int updateReadCount(int boardNo) throws Exception;

	int insertBoardReadLog(String readWho, int boardNo) throws Exception;

	int updateRefByBoardNo(int boardNo) throws Exception;
	// 답글 저장
	int insertReply(HBoardDTO newReply) throws Exception;

	void updateRefOrder(int ref, int refOrder) throws Exception;

}
