package com.miniproject.dao.board;

import java.util.List;

import com.miniproject.model.BoardDetailInfo;
import com.miniproject.model.BoardUpFilesVODTO;
import com.miniproject.model.HBoard;
import com.miniproject.model.HBoardDTO;
import com.miniproject.model.PageRequestDTO;
import com.miniproject.model.PageResponseDTO;

public interface HBoardDAO {
   // 전체 게시글 리스트 얻어오는 (페이징)
   List<HBoard> selectAllHBoard(PageResponseDTO pageResponseDTO) throws Exception;
   
   // 게시글 저장
   int insertHBoard(HBoardDTO newBoard) throws Exception;
   
   // 게시글 첨부 파일 저장
   int insertHBoardUpfile(BoardUpFilesVODTO upFile) throws Exception;

   // 게시글 상세 조회
   BoardDetailInfo selectBoardDetailInfo(int boardNo) throws Exception;
   
   // "?" ip주소를 가진 유저가 ?번글을 24시간 이내에 조회한 기록이 있는지 없는지 체크 
   int checkHourReadLogByBoardNo(String readWho, int boardNo) throws Exception;
   
   // 게시물 조회수 증가
   int updateReadCount(int boardNo) throws Exception;
   
   // 게시물 조회 기록 저장
   int insertBoardReadLog(String readWho, int boardNo) throws Exception;
   
   // 게시글이 insert된후 useGeneratedKeys속성에 의해 얻어진 boardNo를 ref 컬럼에 update
   int updateRefByBoardNo(int boardNo) throws Exception;

   // 답글 저장
   int insertReply(HBoardDTO newReply) throws Exception;

   // 부모글에 대한 다른 답글이 있는 상태에서, 부모글의 새로운 답글이 추가된경우 
   // 새로운 답글이 출력될 공간을 확보한다는 의미에서 기존의 답글에 대해 refOrder값을 1씩 증가시킴
   void updateRefOrder(int ref, int refOrder) throws Exception;
   
   // 게시글의 작성자를 가져오는
   String selectWriterByBoardNo(int boardNo) throws Exception;

   // 게시글 수정
   int updateBoard(HBoardDTO modifyBoard) throws Exception;

   // 게시글 수정시 기존 파일 삭제
   void deleteBoardUpFile(int boardUpFileNo) throws Exception;
   
   // 게시글 삭제시 기존 파일 삭제
   void removeBoardUpFileByBoardNo(int boardNo) throws Exception;

   // 게시글 삭제 처리 (title, content, writer 를 null,   isDelete = 'Y')
   int removeBoardProcess(int boardNo) throws Exception;
 
   // 전체 row의 수 얻기
   int getTotalCountRow() throws Exception;

   // 검색 결과의 row 수 얻기
   int getSearchResultRowCount(PageRequestDTO pageRequestDTO) throws Exception;
}
