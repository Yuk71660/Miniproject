package com.miniproject.dao.reply;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.miniproject.model.PageResponseDTO;
import com.miniproject.model.Reply;
import com.miniproject.model.ReplyDTO;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReplyDAOImpl implements ReplyDAO {

   private final SqlSession ses;
   
   private static final String NS = "com.miniproject.mapper.replymapper";
   
   @Override
   public int insertReply(ReplyDTO replyDTO) throws Exception {
      return ses.insert(NS + ".insertReply", replyDTO);
   }

   @Override
   public Reply selectReplyByReplyNo(int replyNo) throws Exception {
      
      return ses.selectOne(NS + ".selectReplyByReplyNo",  replyNo);
   }

   @Override
   public int getTotalCountRow(int boardNo) throws Exception {
      return ses.selectOne(NS + ".getReplyCountByBoardNo", boardNo);
   }

   @Override
   public List<Reply> selectRepliesByBoardNo(int boardNo, PageResponseDTO<Reply> pageResponseDTO) throws Exception {
      Map<String, Object> params = new HashMap<String, Object>();
      params.put("boardNo", boardNo);
      params.put("startRowIndex", pageResponseDTO.getStartRowIndex());
      params.put("rowCntPerPage", pageResponseDTO.getRowCntPerPage());
   
      return ses.selectList(NS + ".getAllRepliesByBoardNo", params);
   }

   @Override
   public int updateReply(ReplyDTO replyDTO) throws Exception {
      
      return ses.update(NS + ".updateReply", replyDTO);
   }

   @Override
   public int deleteReply(int replyNo) throws Exception {
      
      return ses.delete(NS + ".deleteReply", replyNo);
   }

}
