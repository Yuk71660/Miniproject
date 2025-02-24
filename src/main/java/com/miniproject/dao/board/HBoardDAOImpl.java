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

// DAO단에서 해야 할일
// mapper의 SQL을 호출하여 SQL문을 실행하고 결과를 서비스단으로 반환한다.

@Repository  // 아래의 객체가 DAO객체임을 명시
public class HBoardDAOImpl implements HBoardDAO {

   // HBoardDAOImpl.class에서 사용할 Logger 객체를 얻어옴
   private static Logger logger = LoggerFactory.getLogger(HBoardDAOImpl.class);
   
   // SqlSessionTemplate 객체 주입
   @Autowired
   private SqlSession ses;
   
   private static final String NS = "com.miniproject.mapper.hboardmapper";
   
   @Override
   public List<HBoard> selectAllHBoard() throws Exception {
      logger.info("전체 게시글 얻어오자");
      return ses.selectList(NS + ".getEntireHBoard");
   }

   
   /**
    * @author Administrator
    * @data 2025. 2. 12.
    * @enclosing_method insertHBoard
    * @todo 새로운 게시글(newBoard)를 insert 한 후 pk를 반환
    * @param 
    * @returnType 게시글이 insert된 후 pk값을 newBoard.boardNo에 자동으로 넣어줌
    */
   @Override
   public int insertHBoard(HBoardDTO newBoard) throws Exception {
      ses.insert(NS + ".saveHBoard", newBoard);
      logger.info("지금 저장된 글의 pk : " + newBoard.getBoardNo());
      int result = newBoard.getBoardNo();
      return result;
   }


   @Override
   public int insertHBoardUpfile(BoardUpFilesVODTO upFile) throws Exception {
      
      return ses.insert(NS + ".saveUpFile", upFile);
   }


   @Override
   public BoardDetailInfo selectBoardDetailInfo(int boardNo) throws Exception {
      
      return ses.selectOne(NS + ".getBoardDetailInfoByBoardNo", boardNo);
   }


   @Override
   public int checkHourReadLogByBoardNo(String readWho, int boardNo) throws Exception {
      Map<String, Object> paramMap = new HashMap<String, Object>();
      paramMap.put("readWho", readWho);
      paramMap.put("boardNo", boardNo);
      
      return ses.selectOne(NS + ".checkHourReadLogByBoardNo", paramMap);
   }


   @Override
   public int updateReadCount(int boardNo) throws Exception {
      
      return ses.update(NS + ".updateReadCount", boardNo);
   }


   @Override
   public int insertBoardReadLog(String readWho, int boardNo) throws Exception {
      Map<String, Object> paramMap = new HashMap<String, Object>();
      paramMap.put("readWho", readWho);
      paramMap.put("boardNo", boardNo);
      
      
      return ses.insert(NS + ".insertBoardReadLog", paramMap);
   }


   @Override
   public int updateRefByBoardNo(int boardNo) throws Exception {
      return ses.update(NS + ".updateRefByBoardNo", boardNo);
   }


   @Override
   public int insertReply(HBoardDTO newReply) throws Exception {
      newReply.setStep(newReply.getStep() + 1);
      newReply.setRefOrder(newReply.getRefOrder() + 1);
      return ses.insert(NS + ".insertReply", newReply);
   }


   @Override
   public void updateRefOrder(int ref, int refOrder) throws Exception {
      Map<String, Object> paramMap = new HashMap<String, Object>();
      paramMap.put("ref", ref);
      paramMap.put("refOrder", refOrder);
      
      ses.update(NS + ".updateRefThatWrittenReply", paramMap);
   }


   @Override
   public String selectWriterByBoardNo(int boardNo) throws Exception {
      
      return ses.selectOne(NS + ".getWriterByBoardNo", boardNo);
   }


   @Override
   public int updateBoard(HBoardDTO modifyBoard) throws Exception {
      return ses.update(NS + ".modifyBoard", modifyBoard);
      
   }


   @Override
   public void deleteBoardUpFile(int boardUpFileNo) throws Exception {
      ses.delete(NS + ".removeBoardUpFile", boardUpFileNo);
   }
   
   
   
   

}
