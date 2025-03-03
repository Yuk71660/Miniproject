package com.miniproject.dao.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.miniproject.model.BoardDetailInfo;
import com.miniproject.model.PageRequestDTO;
import com.miniproject.model.PageResponseDTO;
import com.miniproject.model.RBoard;
import com.miniproject.model.RBoardDTO;

@Repository
public class RBoardDAOImpl implements RBoardDAO {
   

   // SqlSessionTemplate 객체 주입
   @Autowired
   private SqlSession ses;

   private static final String NS = "com.miniproject.mapper.rboardmapper";

   @Override
   public List<RBoard> selectAllHBoard(PageResponseDTO<RBoard> pageResponseDTO) throws Exception {
      // TODO Auto-generated method stub
      return ses.selectList(NS + ".getEntireRBoard", pageResponseDTO);
   }

   @Override
   public int getTotalCountRow() throws Exception {
      
      return ses.selectOne(NS + ".getTotalCountRow");
   }


   @Override
   public int getSearchResultRowCount(PageRequestDTO pageRequestDTO) throws Exception {
      pageRequestDTO.setSearchWord("%" + pageRequestDTO.getSearchWord() + "%");  // like 검색을 위해
      return ses.selectOne(NS + ".getSearchResultCountRow", pageRequestDTO);
   }

   @Override
   public int insertHBoard(RBoardDTO newBoard) throws Exception {
      return ses.insert(NS + ".saveHBoard", newBoard);
   }
   
   @Override
   public int checkHourReadLogByBoardNo(String readWho, int boardNo, String boardType) throws Exception {
      Map<String, Object> paramMap = new HashMap<String, Object>();
      paramMap.put("readWho", readWho);
      paramMap.put("boardNo", boardNo);
      paramMap.put("boardType", boardType);
      
      return ses.selectOne(NS + ".checkHourReadLogByBoardNo", paramMap);
   }

   @Override
   public int updateReadCount(int boardNo) throws Exception {
      
      return ses.update(NS + ".updateReadCount", boardNo);
   }
   
   @Override
   public int insertBoardReadLog(String readWho, int boardNo, String boardType) throws Exception {
      Map<String, Object> paramMap = new HashMap<String, Object>();
      paramMap.put("readWho", readWho);
      paramMap.put("boardNo", boardNo);
      paramMap.put("boardType", boardType);
      
      
      return ses.insert(NS + ".insertBoardReadLog", paramMap);
   }
   
   @Override
   public BoardDetailInfo selectBoardDetailInfo(int boardNo) throws Exception {
      
      return ses.selectOne(NS + ".getBoardDetailInfoByBoardNo", boardNo);
   }
   
   @Override
   public String selectWriterByBoardNo(int boardNo) throws Exception {
      
      return ses.selectOne(NS + ".getWriterByBoardNo", boardNo);
   }

   @Override
   public int updateBoard(RBoardDTO modifyBoard) throws Exception {
      return ses.update(NS + ".modifyBoard", modifyBoard);
      
   }

   @Override
   public int deleteBoard(int boardNo) throws Exception {
      return ses.delete(NS + ".removeBoard", boardNo);
   }

}
