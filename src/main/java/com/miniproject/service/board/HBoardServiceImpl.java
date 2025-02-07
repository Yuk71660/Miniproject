package com.miniproject.service.board;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miniproject.dao.board.HBoardDAO;
import com.miniproject.model.HBoard;
import com.miniproject.model.HBoardDTO;

//서비스에서 해야 할 일
// 서비스 비즈니스 로직 처리
// 트랜잭션 처리
// DAO단으로 넘겨주기

@Service
public class HBoardServiceImpl implements HBoardService {

	private static Logger logger = LoggerFactory.getLogger(HBoardServiceImpl.class);
	
	@Autowired
	private HBoardDAO dao;
	
	@Override
	public List<HBoard> getEntireHBoard() throws Exception {
		// TODO Auto-generated method stub
		logger.info("게시글 전체 리스트 가져오기");
		
		List<HBoard> list = dao.selectAllHBoard();
		for (HBoard h: list) {
			System.out.println(h.toString());
		}
		
		return list;
		
//		return dao.selectAllHBoard();
	}

	@Override
	public boolean saveBoard(HBoardDTO newBoard) throws Exception {
		boolean result = false;
		if (dao.insertHBoard(newBoard) == 1) {
			result = true;
		}
		System.out.println("저장?" + result);
		return result;
	}

}
