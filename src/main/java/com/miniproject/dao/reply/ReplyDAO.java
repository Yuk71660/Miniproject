package com.miniproject.dao.reply;

import java.util.List;

import com.miniproject.model.PageResponseDTO;
import com.miniproject.model.Reply;
import com.miniproject.model.ReplyDTO;

public interface ReplyDAO {
   int insertReply(ReplyDTO replyDTO) throws Exception;

   Reply selectReplyByReplyNo(int replyNo) throws Exception;

   int getTotalCountRow(int boardNo) throws Exception;

   List<Reply> selectRepliesByBoardNo(int boardNo, PageResponseDTO<Reply> pageResponseDTO) throws Exception;
}
