package com.miniproject.controller.replies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miniproject.model.ReplyDTO;

@RestController  // view 페이지를 응답하지 않는 메서드만을 가진 컨트롤러
@RequestMapping("/replies")
public class ReplyController {
   
   private static final Logger logger = LoggerFactory.getLogger(ReplyController.class);
   
   // @PathVariable : URI에 달려있는 변수를 의미
   // @RequestBody : JSON 형태로 요청 데이터를 받음
   @PostMapping("/{boardNo}")
   public void registerReply(@PathVariable("boardNo")int boardNo, @RequestBody ReplyDTO replyDTO) {
      logger.info(replyDTO.toString() + "의 댓글을 " + boardNo + "번의 댓글로 저장하자");
      
   }
}
