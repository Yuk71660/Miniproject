package com.miniproject.service.board;

import com.miniproject.model.BoardDetailInfo;
import com.miniproject.model.HBoardDTO;
import com.miniproject.model.PageRequestDTO;
import com.miniproject.model.PageResponseDTO;
import com.miniproject.model.RBoard;
import com.miniproject.model.RBoardDTO;

public interface RBoardService {
   // 전체 게시글 리스트 얻어오는
   PageResponseDTO<RBoard> getEntireHBoard(PageRequestDTO pageRequestDTO) throws Exception;

   boolean saveBoard(RBoardDTO newBoard) throws Exception;

   BoardDetailInfo getBoardDetailInfo(int boardNo, String clientIp) throws Exception;

   boolean modifyBoard(RBoardDTO modifyBoard) throws Exception;

   boolean removeBoard(int boardNo) throws Exception;
}
