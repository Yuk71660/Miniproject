package com.miniproject.dao.board;

import java.util.List;

import com.miniproject.model.BoardUpFilesVODTO;
import com.miniproject.model.HBoard;
import com.miniproject.model.HBoardDTO;

public interface HBoardDAO {
	
	List<HBoard> selectAllHBoard() throws Exception;

	int insertHBoard(HBoardDTO newBoard) throws Exception;
	
	int insertHBoardUpfile(BoardUpFilesVODTO upFile) throws Exception;

}
