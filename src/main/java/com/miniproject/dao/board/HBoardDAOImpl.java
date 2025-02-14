package com.miniproject.dao.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.miniproject.model.BoardDetailInfo;
import com.miniproject.model.BoardUpFilesVODTO;
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

	/**
	 * @author Administrator
	 * @data 2025. 2. 12.
	 * @enclosing_method insertHBoard
	 * @todo 새로운 게시글 insert
	 * @param
	 * @returnType pk값을 반환 
	 */
	@Override
	public int insertHBoard(HBoardDTO newBoard) throws Exception {
		// TODO Auto-generated method stub
		ses.insert(NS+".saveHBoard", newBoard);
		return newBoard.getBoardNo();
	}
	
	@Override
	public int updateRefByBoardNo(int boardNo) throws Exception {
		// TODO Auto-generated method stub
		return ses.update(NS+".updateRefByBoardNo", boardNo);
	}

	@Override
	public int insertHBoardUpfile(BoardUpFilesVODTO upFile) throws Exception {
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println(upFile.toString());
		
		return ses.insert(NS + ".saveUpFile", upFile);
	}

	@Override
	public BoardDetailInfo selectBoardDetailInfo(int boardNo) throws Exception {
		// TODO Auto-generated method stub
		return ses.selectOne(NS+".getBoardDetailInfoByBoardNo", boardNo);
	}

	@Override
	public int checkHourReadLogByBoardNo(String readWho, int boardNo) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("readWho", readWho);
		paramMap.put("boardNo", boardNo);
		
		return ses.selectOne(NS + ".checkHourReadLogByBoardNo", paramMap);
	}
	
	@Override
	public int updateReadCount(int boardNo) throws Exception  {
		return ses.update(NS + ".updateReadCount", boardNo);
	}
	
	@Override
	public int insertBoardReadLog(String readWho, int boardNo) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("readWho", readWho);
		paramMap.put("boardNo", boardNo);
		
		return ses.insert(NS + ".insertBoardReadLog", paramMap);
	}

}
