package com.miniproject.service.board;

import java.util.List;

import com.miniproject.model.HBoard;

public interface HBoardService {
	List<HBoard> getEntireHBoard() throws Exception;
}
