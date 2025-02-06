package com.miniproject.dao.board;

import java.util.List;

import com.miniproject.model.Hboard;

public interface HBoardDAO {
	
	List<Hboard> selectAllHBoard() throws Exception;

}
