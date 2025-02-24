package com.miniproject.service.board;

import java.util.List;

import org.ietf.jgss.ChannelBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.miniproject.dao.board.HBoardDAO;
import com.miniproject.dao.member.MemberDAO;
import com.miniproject.dao.pointlog.PointLogDAO;
import com.miniproject.model.BoardDetailInfo;
import com.miniproject.model.BoardUpFilesVODTO;
import com.miniproject.model.FileStatus;
import com.miniproject.model.HBoard;
import com.miniproject.model.HBoardDTO;
import com.miniproject.model.PointLogDTO;

import lombok.RequiredArgsConstructor;

// 서비스 객체에서 해야 할 일
// 1) 서비스 비즈니스 로직 처리 하는 부분...
// 2) 트랜잭션 처리
// 컨트롤러단에서 넘겨받은 매개변수를 DAO단으로 넘겨주기 전에 비즈니스 로직을 처리한다.


@Service  // 아래의 객체가 서비스 객체임을 명시
@RequiredArgsConstructor
public class HBoardServiceImpl implements HBoardService {

   // HBoardServiceImpl.class에서 사용할 Logger 객체를 얻어옴
   private static Logger logger = LoggerFactory.getLogger(HBoardServiceImpl.class);
   
   
   private final HBoardDAO hdao;
   private final PointLogDAO pdao;
   private final MemberDAO mdao;
   
   
   @Override
   public List<HBoard> getEntireHBoard() throws Exception {
      logger.info("게시글 전체 리스트 얻어오자");
      
      List<HBoard> list = hdao.selectAllHBoard();

      
      return list;
   }

   @Override
   @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
   public boolean saveBoard(HBoardDTO newBoard, List<BoardUpFilesVODTO> fileList) throws Exception {
      boolean result = false;
      // 게시글을 저장하는 트랜잭션
      // 트랜잭션의 기본 원칙 : All commit or Nothing => 전부 성공할 때 all commit, 하나라도 실패하면 rollback
      
      newBoard.setContent(newBoard.getContent().replace("\n", "<br />").replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;"));
      
      if (hdao.insertHBoard(newBoard) > 0) { // hBoard테이블에 insert 
         
         int newBoardNo = newBoard.getBoardNo(); // insert된 pk(boardNo) 값을 얻어와
         hdao.updateRefByBoardNo(newBoardNo);  // pk(boardNo) 값을 ref에 update
         
         // 업로드 한 파일이 있다면.. 파일업로드 (insert)
         if (fileList.size() > 0) {
            
            for (BoardUpFilesVODTO file : fileList) {
               file.setBoardNo(newBoardNo);  // boardNo를 첨부 파일에 저장
//               System.out.println(file.toString());
               hdao.insertHBoardUpfile(file);
            }
         }
         
         // 글 작성자에게 포인트 지급 (pointlog테이블 insert)
         PointLogDTO dto = PointLogDTO.builder()
               .pointwho(newBoard.getWriter())
               .pointwhy("게시글작성")
               .build();
                     
         if (pdao.insertPointLog(dto) == 1) {
            // 글 작성자 포인트 업데이트(member테이블에 userpoint update)
            if (mdao.updateUserPoint(newBoard.getWriter(), "게시글작성") == 1) {
               result = true;
            }
         }
         
      }
      return result;
   }

   @Override
   @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
   public BoardDetailInfo getBoardDetailInfo(int boardNo, String ipAddr) throws Exception {
      
      int timediff = hdao.checkHourReadLogByBoardNo(ipAddr, boardNo);
      logger.info("조회 시간 차 : " + timediff);
      if (timediff == -1 || timediff >= 24) {  // 이전 조회기록이 없거나, 24시간 이상 지났을 경우
         // 조회수 증가
         if (hdao.updateReadCount(boardNo) == 1) {
            // 조회 기록 insert
            hdao.insertBoardReadLog(ipAddr, boardNo);
         }
         
      } 
      
      BoardDetailInfo bi = hdao.selectBoardDetailInfo(boardNo);
//      System.out.println(bi.toString());
      
      // 업로드 파일이 없는 게시글일 경우 join문에 의해 만들어진 가짜 BoardUpFilesVODTO 객체를 리스트에서 삭제
      int index = -1;
      List<BoardUpFilesVODTO> lst = bi.getFileList();
      for (int i = 0; i < bi.getFileList().size(); i++) {
         if (lst.get(i).getBoardUpFileNo() == 0) {
            index = i;
         }
      }
   
      if (index > -1) {
         lst.remove(index);
         bi.setFileList(lst);
      }
      
      
      return bi;
   }

   @Override
   @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
   public boolean saveReply(HBoardDTO newReply) throws Exception {
      
//      부모글에 대한 다른 답글이 있는 상태에서, 부모글의 새로운 답글이 추가된경우 
//      -- 새로운 답글이 출력될 공간을 확보한다는 의미에서 기존의 답글에 대해 refOrder값을 1씩 증가시킴
      hdao.updateRefOrder(newReply.getRef(), newReply.getRefOrder());
      
      // newReply를 저장할 때, ref : 부모글의 ref, step : 부모글의 step +1, refOrder : 부모글의 refOrder + 1로 저장
      hdao.insertReply(newReply);
      
      return true;
   }

   @Override
   @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
   public boolean modifyBoard(HBoardDTO modifyBoard, List<BoardUpFilesVODTO> modifyFileList) throws Exception {
      
      if (hdao.updateBoard(modifyBoard) == 1) {
         for (BoardUpFilesVODTO f: modifyFileList) {
            if (f.getStatus() == FileStatus.NEW) {
               f.setBoardNo(modifyBoard.getBoardNo());
               hdao.insertHBoardUpfile(f);
            }
            
            if (f.getStatus() == FileStatus.DELETE) {
               hdao.deleteBoardUpFile(f.getBoardUpFileNo());
            }
         }
      }
      
      return true;
   }

}
