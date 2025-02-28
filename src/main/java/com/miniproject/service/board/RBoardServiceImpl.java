package com.miniproject.service.board;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.miniproject.dao.board.RBoardDAO;
import com.miniproject.model.PageRequestDTO;
import com.miniproject.model.PageResponseDTO;
import com.miniproject.model.RBoard;
import com.mysql.cj.util.StringUtils;

import lombok.RequiredArgsConstructor;

@Service  // 아래의 객체가 서비스 객체임을 명시
@RequiredArgsConstructor
public class RBoardServiceImpl implements RBoardService {
   
   private static Logger logger = LoggerFactory.getLogger(RBoardServiceImpl.class);
   
   private final RBoardDAO rdao;

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

}
