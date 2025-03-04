package com.miniproject.service.reply;

import com.miniproject.model.ReplyDTO;

public interface ReplyService {
   boolean registerReply(ReplyDTO replyDTO) throws Exception;
}
