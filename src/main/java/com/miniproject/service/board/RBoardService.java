package com.miniproject.service.board;

import com.miniproject.model.PageRequestDTO;
import com.miniproject.model.PageResponseDTO;
import com.miniproject.model.RBoard;

public interface RBoardService {
   // 전체 게시글 리스트 얻어오는
   PageResponseDTO<RBoard> getEntireHBoard(PageRequestDTO pageRequestDTO) throws Exception;
}