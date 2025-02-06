package com.miniproject.dao.board;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.miniproject.model.HBoard;
import com.miniproject.model.HBoardDTO;

@Repository
public class HBoardDAOImpl implements HBoardDAO {
	
	private static Logger logger = LoggerFactory.getLogger(HBoardDAOImpl.class);
	
	@Autowired
	private SqlSession ses;
	
	private static final String NS = "com.miniproject.mapper.hboardmapper";

	@Override
	public List<HBoard> selectAllHBoard() throws Exception {
		// TODO Auto-generated method stub
		logger.info("게시글 DAO Test");
		return ses.selectList(NS + ".getEntireHBoard");
	}

	@Override
	public int insertHBoard(HBoardDTO newBoard) throws Exception {
		// TODO Auto-generated method stub
		return ses.insert(NS+".saveHBoard", newBoard);
	}

}
