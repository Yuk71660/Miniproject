package com.miniproject.dao.board;

import java.util.List;

import com.miniproject.model.PageRequestDTO;
import com.miniproject.model.PageResponseDTO;
import com.miniproject.model.RBoard;

public interface RBoardDAO {
   // 전체 게시글 리스트 얻어오는 (페이징)
   List<RBoard> selectAllHBoard(PageResponseDTO<RBoard> pageResponseDTO) throws Exception;

   // 전체 row의 수 얻기
   int getTotalCountRow() throws Exception;

   // 검색 결과의 row 수 얻기
   int getSearchResultRowCount(PageRequestDTO pageRequestDTO) throws Exception;

}
