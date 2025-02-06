package com.miniproject.dao.board;

import java.util.List;

import com.miniproject.model.HBoard;

public interface HBoardDAO {
	
	List<HBoard> selectAllHBoard() throws Exception;

}
