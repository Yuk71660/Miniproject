package com.miniproject.service.board;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.miniproject.dao.board.RBoardDAO;
import com.miniproject.dao.member.MemberDAO;
import com.miniproject.dao.pointlog.PointLogDAO;
import com.miniproject.model.BoardDetailInfo;
import com.miniproject.model.BoardUpFilesVODTO;
import com.miniproject.model.FileStatus;
import com.miniproject.model.HBoardDTO;
import com.miniproject.model.PageRequestDTO;
import com.miniproject.model.PageResponseDTO;
import com.miniproject.model.PointLogDTO;
import com.miniproject.model.RBoard;
import com.miniproject.model.RBoardDTO;
import com.mysql.cj.util.StringUtils;

import lombok.RequiredArgsConstructor;

@Service  // 아래의 객체가 서비스 객체임을 명시
@RequiredArgsConstructor
public class RBoardServiceImpl implements RBoardService {
   
   private static Logger logger = LoggerFactory.getLogger(RBoardServiceImpl.class);
   
   private final RBoardDAO rdao;
   private final PointLogDAO pdao;
   private final MemberDAO mdao;

   @Override
   public PageResponseDTO<RBoard> getEntireHBoard(PageRequestDTO pageRequestDTO) throws Exception {
      logger.info("게시글 전체 리스트 얻어오자");

      // 넘겨 받은 pageRequestDTO를 이용해 페이징을 할 수 있도록 처리
      PageResponseDTO<RBoard> pageResponseDTO = pagingProcess(pageRequestDTO);

      List<RBoard> list = rdao.selectAllHBoard(pageResponseDTO);
      pageResponseDTO.setBoardList(list);

      return pageResponseDTO;
   }

   private PageResponseDTO<RBoard> pagingProcess(PageRequestDTO pageRequestDTO) throws Exception {
      PageResponseDTO<RBoard> pageResponseDTO = new PageResponseDTO<RBoard>(pageRequestDTO.getPageNo(),
            pageRequestDTO.getRowCntPerPage());

      // 기본 페이징
      if (StringUtils.isNullOrEmpty(pageRequestDTO.getSearchType())) {
         // 검색 안함
         pageResponseDTO.setTotalRowCnt(rdao.getTotalCountRow()); // 전체 데이터 수
      } else if (!StringUtils.isNullOrEmpty(pageRequestDTO.getSearchType())) {
         // 검색 함
         pageResponseDTO.setSearchType(pageRequestDTO.getSearchType());
         pageResponseDTO.setSearchWord(pageRequestDTO.getSearchWord());

         pageResponseDTO.setTotalRowCnt(rdao.getSearchResultRowCount(pageRequestDTO));
      }

      pageResponseDTO.setTotalPageCnt(); // 전체 페이지 수
      pageResponseDTO.setStartRowIndex(); // 출력 시작할 rowIndex번호

      // 페이징 블럭을 표시하기 위해
      pageResponseDTO.setBlockOfCurrentPage(); // 현재 페이지가 몇번째 블럭에 있는가?
      pageResponseDTO.setStartPageNumPerBlock(); // 블럭에서의 시작페이지 번호
      pageResponseDTO.setEndPageNumPerBlock(); // 블럭에서의 끝페이지 번호

      return pageResponseDTO;
   }

   @Override
   public boolean saveBoard(RBoardDTO newBoard) throws Exception {
      boolean result = false;
      // 게시글을 저장하는 트랜잭션
      // 트랜잭션의 기본 원칙 : All commit or Nothing => 전부 성공할 때 all commit, 하나라도 실패하면 rollback
      
      newBoard.setContent(newBoard.getContent().replace("\n", "<br />").replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;"));
      
      if (rdao.insertHBoard(newBoard) > 0) { // hBoard테이블에 insert 
         
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
      
      int timediff = rdao.checkHourReadLogByBoardNo(ipAddr, boardNo, "rboard");
      logger.info("조회 시간 차 : " + timediff);
      if (timediff == -1 || timediff >= 24) {  // 이전 조회기록이 없거나, 24시간 이상 지났을 경우
         // 조회수 증가
         if (rdao.updateReadCount(boardNo) == 1) {
            // 조회 기록 insert
            rdao.insertBoardReadLog(ipAddr, boardNo, "rboard");
         }
         
      } 
      
      BoardDetailInfo bi = rdao.selectBoardDetailInfo(boardNo);
//      System.out.println(bi.toString());
      
      return bi;
   }

   @Override
   @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
   public boolean modifyBoard(RBoardDTO modifyBoard) throws Exception {
      boolean result = false;
      
      if (rdao.updateBoard(modifyBoard) == 1)  {
         result = true;
      }
      
      return result;
   }

   @Override
   public boolean removeBoard(int boardNo) throws Exception {
      
      return (rdao.deleteBoard(boardNo) == 1)? true : false;
   }

}
