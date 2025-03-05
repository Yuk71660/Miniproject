package com.miniproject.controller.reply;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miniproject.model.MyResponseWithData;
import com.miniproject.model.MyResponseWithoutData;
import com.miniproject.model.PageRequestDTO;
import com.miniproject.model.PageResponseDTO;
import com.miniproject.model.Reply;
import com.miniproject.model.ReplyDTO;
import com.miniproject.service.reply.ReplyService;

import lombok.RequiredArgsConstructor;

@RestController // view 페이지를 응답하지 않는 메서드만을 가진 컨트롤러
@RequestMapping("/replies")
@RequiredArgsConstructor
public class ReplyController {

   private final ReplyService service;

   private static final Logger logger = LoggerFactory.getLogger(ReplyController.class);

   // @PathVariable : URI에 달려있는 변수를 의미
   // @RequestBody : JSON 형태로 요청 데이터를 받음
   @PostMapping(value = "/{boardNo}", produces = "application/json; charset=utf-8")
   public ResponseEntity<MyResponseWithoutData> registerReply(@PathVariable("boardNo") int boardNo,
         @RequestBody ReplyDTO replyDTO) {
      logger.info(replyDTO.toString() + "의 댓글을 " + boardNo + "번의 댓글로 저장하자");

      replyDTO.setBoardNo(boardNo);
      ResponseEntity<MyResponseWithoutData> result = null;

      try {
         if (service.registerReply(replyDTO)) {
            result = ResponseEntity.ok(MyResponseWithoutData.successResponse());
         }
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         result = ResponseEntity.badRequest().body(MyResponseWithoutData.failureResponse());
      }

      return result;
   }

   // /{replyNo} 로 GetMapping 하게 되면 readAllReplies()와 매핑주소와 상태가 같게 되므로
   // 모호(ambiguously)해지므로 uri를 바꿈
   @GetMapping(value = "/reply/{replyNo}", produces = "application/json; charset=utf-8")
   public ResponseEntity<MyResponseWithData<Reply>> readReply(@PathVariable("replyNo") int replyNo) {
      logger.info(replyNo + "번 댓글을 조회하자");

      Reply reply = null;

      ResponseEntity<MyResponseWithData<Reply>> result = null;
      MyResponseWithData<Reply> mw = new MyResponseWithData<Reply>();
      try {
         reply = service.readReply(replyNo);
         if (reply != null) {

            mw.setData(reply);
            mw.setCode(200);
            mw.setMessage("success");
            result = ResponseEntity.ok(mw);
         }
      } catch (Exception e) {
         e.printStackTrace();

         if (e instanceof NoSuchElementException) {
            mw.setCode(404);
            mw.setMessage(e.getMessage());
            mw.setData(null);
         } else {
            mw.setCode(500);
            mw.setMessage("fail : " + e.getMessage());
            mw.setData(null);

         }

         result = ResponseEntity.badRequest().body(mw);
      }

      return result;
   }

   @GetMapping(value = "/{boardNo}", produces = "application/json; charset=utf-8")
   public ResponseEntity<MyResponseWithData<PageResponseDTO<Reply>>> readAllReplies(
         @PathVariable("boardNo") int boardNo, PageRequestDTO pageRequestDTO) {
      pageRequestDTO.setPageNo(1);
      pageRequestDTO.setRowCntPerPage(3);
      logger.info(boardNo + "번에 대한 모든 댓글을 가져오자");

      ResponseEntity<MyResponseWithData<PageResponseDTO<Reply>>> result = null;
      MyResponseWithData<PageResponseDTO<Reply>> mw = new MyResponseWithData<PageResponseDTO<Reply>>();
      try {
         PageResponseDTO<Reply> data = service.getAllReplies(boardNo, pageRequestDTO);

         mw.setCode(200);
         mw.setData(data);
         mw.setMessage("success");

         result = new ResponseEntity<MyResponseWithData<PageResponseDTO<Reply>>>(mw, HttpStatus.OK);

      } catch (Exception e) {

         if (e instanceof NoSuchElementException) {
            mw.setCode(404);
            mw.setMessage(e.getMessage());
            mw.setData(null);

         } else {
            e.printStackTrace();
            mw.setCode(500);
            mw.setData(null);
            mw.setMessage("fail : " + e.getMessage());

         }

         result = ResponseEntity.badRequest().body(mw);

      }

      return result;
   }

   @PutMapping(value = "/{replyNo}", produces = "application/json; charset=utf-8")
   public ResponseEntity<MyResponseWithoutData> modifyReply(@PathVariable("replyNo") int replyNo,
         @RequestBody ReplyDTO replyDTO) {
      replyDTO.setReplyNo(replyNo);
      logger.info(replyDTO.getReplyNo() + "번 댓글을 " + replyDTO.getReplyContent() + "로 수정");

      ResponseEntity<MyResponseWithoutData> result = null;

      try {
         if (service.modifyReply(replyDTO)) {
            result = new ResponseEntity<MyResponseWithoutData>(MyResponseWithoutData.successResponse(),
                  HttpStatus.OK);
         }
      } catch (Exception e) {
         e.printStackTrace();
         if (e instanceof NoSuchElementException) {
            result = new ResponseEntity<MyResponseWithoutData>(new MyResponseWithoutData(404, e.getMessage()),
                  HttpStatus.NOT_FOUND);
         } else {
            result = new ResponseEntity<MyResponseWithoutData>(MyResponseWithoutData.failureResponse(),
                  HttpStatus.INTERNAL_SERVER_ERROR);
         }

      }
      return result;
   }

   @DeleteMapping(value = "/{replyNo}", produces = "application/json; charset=utf-8")
   public ResponseEntity<MyResponseWithoutData> removeReply(@PathVariable("replyNo") int replyNo) {
      logger.info(replyNo + "번 댓글 삭제");

      ResponseEntity<MyResponseWithoutData> result = null;

      try {
         if (service.removeReply(replyNo)) {
            result = new ResponseEntity<MyResponseWithoutData>(MyResponseWithoutData.successResponse(),
                  HttpStatus.OK);
         }
      } catch (Exception e) {
         e.printStackTrace();
         if (e instanceof NoSuchElementException) {
            result = new ResponseEntity<MyResponseWithoutData>(new MyResponseWithoutData(404, e.getMessage()),
                  HttpStatus.NOT_FOUND);
         } else {
            result = new ResponseEntity<MyResponseWithoutData>(MyResponseWithoutData.failureResponse(),
                  HttpStatus.INTERNAL_SERVER_ERROR);
         }

      }
      
      return result;
   }

}
