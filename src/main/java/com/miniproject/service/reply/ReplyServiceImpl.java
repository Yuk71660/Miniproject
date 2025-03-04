package com.miniproject.service.reply;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.miniproject.dao.member.MemberDAO;
import com.miniproject.dao.pointlog.PointLogDAO;
import com.miniproject.dao.reply.ReplyDAO;
import com.miniproject.model.PointLogDTO;
import com.miniproject.model.ReplyDTO;
import com.miniproject.service.board.HBoardServiceImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{

   private final ReplyDAO dao;
   private final PointLogDAO pdao;
   private final MemberDAO mdao;

   
   @Override
   @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
   public boolean registerReply(ReplyDTO replyDTO) throws Exception {
      boolean result = false;
      
      if (dao.insertReply(replyDTO) == 1) {
         savePoint(replyDTO.getReplyer(), "답글/댓글작성");
         result = true;
      }
      return result; 
   }
   
   // HBoardServiceImpl에도 savePoint()가 있다. 그 메서드를 재사용할 수 없는 이유 
   // HBoardServiceImpl 객체를 같은 Service 객체에서 주입 받지 못한다.
   // savePoint의 실제 작업을 수행하는 주체가 Member객체, PointLog 객체가 아닌 Mybatis이기 때문이다...
   private void savePoint(String pointWho, String why) throws Exception {
      // 글 작성자에게 포인트 지급 (pointlog테이블 insert)
      PointLogDTO dto = PointLogDTO.builder()
            .pointwho(pointWho)
            .pointwhy(why)
            .build();
                  
      if (pdao.insertPointLog(dto) == 1) {
         // 글 작성자 포인트 업데이트(member테이블에 userpoint update)
         mdao.updateUserPoint(pointWho, why);
      }
   }
   
}

