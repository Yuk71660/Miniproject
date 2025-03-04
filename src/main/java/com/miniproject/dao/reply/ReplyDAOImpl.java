package com.miniproject.dao.reply;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

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

}
