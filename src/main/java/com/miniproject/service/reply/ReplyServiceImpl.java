package com.miniproject.service.reply;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.miniproject.dao.member.MemberDAO;
import com.miniproject.dao.pointlog.PointLogDAO;
import com.miniproject.dao.reply.ReplyDAO;
import com.miniproject.model.PageRequestDTO;
import com.miniproject.model.PageResponseDTO;
import com.miniproject.model.PointLogDTO;
import com.miniproject.model.Reply;
import com.miniproject.model.ReplyDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

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
      PointLogDTO dto = PointLogDTO.builder().pointwho(pointWho).pointwhy(why).build();

      if (pdao.insertPointLog(dto) == 1) {
         // 글 작성자 포인트 업데이트(member테이블에 userpoint update)
         mdao.updateUserPoint(pointWho, why);
      }
   }

   @Override
   @Transactional(isolation = Isolation.DEFAULT, readOnly = true)
   public Reply readReply(int replyNo) throws Exception {
      Reply reply = dao.selectReplyByReplyNo(replyNo);
      if (reply == null) {
         throw new NoSuchElementException(replyNo + "번 데이터는 존재하지 않습니다");
      }
      return reply;
   }

   @Override
   public PageResponseDTO<Reply> getAllReplies(int boardNo, PageRequestDTO pageRequestDTO) throws Exception {
      PageResponseDTO<Reply> pageResponseDTO =  pagingProcess(boardNo, pageRequestDTO);
      List<Reply> list = dao.selectRepliesByBoardNo(boardNo, pageResponseDTO);
      if(list.size() == 0) {
         throw new NoSuchElementException(boardNo + "번에 대한 댓글이 존재하지 않습니다");
      }
      pageResponseDTO.setBoardList(list);
      return pageResponseDTO;
   }

   private PageResponseDTO<Reply> pagingProcess(int boardNo, PageRequestDTO pageRequestDTO) throws Exception {
      PageResponseDTO<Reply> pageResponseDTO = new PageResponseDTO<Reply>(pageRequestDTO.getPageNo(),
            pageRequestDTO.getRowCntPerPage());

      // 기본 페이징
      pageResponseDTO.setTotalRowCnt(dao.getTotalCountRow(boardNo)); // 전체 데이터 수
      pageResponseDTO.setTotalPageCnt(); // 전체 페이지 수
      pageResponseDTO.setStartRowIndex(); // 출력 시작할 rowIndex번호

      // 페이징 블럭을 표시하기 위해
      pageResponseDTO.setBlockOfCurrentPage(); // 현재 페이지가 몇번째 블럭에 있는가?
      pageResponseDTO.setStartPageNumPerBlock(); // 블럭에서의 시작페이지 번호
      pageResponseDTO.setEndPageNumPerBlock(); // 블럭에서의 끝페이지 번호

      return pageResponseDTO;
   }

}
