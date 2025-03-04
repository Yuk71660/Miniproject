package com.miniproject.controller.reply;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miniproject.model.MyResponseWithoutData;
import com.miniproject.model.ReplyDTO;
import com.miniproject.service.reply.ReplyService;

import lombok.RequiredArgsConstructor;

@RestController  // view 페이지를 응답하지 않는 메서드만을 가진 컨트롤러
@RequestMapping("/replies")
@RequiredArgsConstructor
public class ReplyController {
   
   private final ReplyService service;
   
   private static final Logger logger = LoggerFactory.getLogger(ReplyController.class);
   
   // @PathVariable : URI에 달려있는 변수를 의미
   // @RequestBody : JSON 형태로 요청 데이터를 받음
   @PostMapping(value="/{boardNo}", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<MyResponseWithoutData> registerReply(@PathVariable("boardNo")int boardNo, @RequestBody ReplyDTO replyDTO) {
      logger.info(replyDTO.toString() + "의 댓글을 " + boardNo + "번의 댓글로 저장하자");
      
      replyDTO.setBoardNo(boardNo); 
      ResponseEntity<MyResponseWithoutData> result = null;
      
      try {
         if(service.registerReply(replyDTO)) {
            result = ResponseEntity.ok(MyResponseWithoutData.successResponse());
         }
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         result = ResponseEntity.badRequest().body(MyResponseWithoutData.failureResponse());
      }
      
      return result;
   }
}
