package com.miniproject.dao.reply;

import com.miniproject.model.Reply;
import com.miniproject.model.ReplyDTO;

public interface ReplyDAO {
   int insertReply(ReplyDTO replyDTO) throws Exception;

   Reply selectReplyByReplyNo(int replyNo) throws Exception;
}