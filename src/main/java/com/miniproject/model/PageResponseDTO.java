package com.miniproject.model;

import java.util.List;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Administrator
 * @date 2025. 2. 27.
 * @packagename com.miniproject.model
 * @typeName PageResponseDTO
 * @todo 페이징 응답에 필요한 데이터를 가지고 있는 DTO 클래스
 * 
 */
@Getter
@ToString
public class PageResponseDTO {
   private int pageNo;  // 현재 페이지 번호
   private int rowCntPerPage; // 1페이지당 보여줄 row갯수
   private String searchType;
   private String searchWord;
   
   private int totalRowCnt; // 전체 데이터 수
   private int totalPageCnt;  // 전체 페이지 수
   private int startRowIndex; // 출력을 시작할 rowIndex
   
   private int pageCntPerBlock = 10; // 1블럭당 보여줄 페이지 수
   private int blockOfCurrentPage; // 현재 페이지가 몇번째 블럭인지
   private int startPageNumPerBlock; // 그 블럭의 시작 페이지 번호
   private int endPageNumPerBlock; // 그 블럭의 끝 페이지 번호
   
   private List<HBoard> boardList;
   
   public PageResponseDTO(int pageNo, int rowCntPerPage) {
      this.pageNo = pageNo;
      this.rowCntPerPage = rowCntPerPage;
   }
   
   public void setSearchType(String searchType) {
      this.searchType = searchType;
   }
   
   public void setSearchWord(String searchWord) {
      this.searchWord = searchWord;
   }
   
   //  전체 데이터 수
   public void setTotalRowCnt(int totalRowCnt) {
      this.totalRowCnt = totalRowCnt;
   }
   
   // 전체 페이지 수
   public void setTotalPageCnt() { 
      // 전체 데이터 갯수 / 한페이지당 출력할 row의 수
      // 나누어 떨어지지 않으면 +1
      
      if (this.totalRowCnt % this.rowCntPerPage == 0) {
         this.totalPageCnt = this.totalRowCnt / this.rowCntPerPage;
      } else {
         this.totalPageCnt = (this.totalRowCnt / this.rowCntPerPage) + 1;
      }
   }
   
   public void setStartRowIndex() {
      // 페이지에서출력하기시작할row의 index번호 = (현재페이지번호 - 1) * 한페이지당 출력할 row의 수
      this.startRowIndex = (this.pageNo - 1) * this.rowCntPerPage;
   }
   
   
   // 현재 페이지가 몇번째 블럭에 있는가?
   public void setBlockOfCurrentPage() {
      if (this.pageNo % pageCntPerBlock == 0) {
         this.blockOfCurrentPage = this.pageNo / this.pageCntPerBlock;
      } else {
         this.blockOfCurrentPage = (this.pageNo / this.pageCntPerBlock) + 1; 
      }      
   }
   
   // 블럭에서의 시작페이지 번호
   public void setStartPageNumPerBlock() {
      this.startPageNumPerBlock = ((this.blockOfCurrentPage - 1) * this.pageCntPerBlock) + 1;
   }
   
   // 블럭에서의 끝페이지 번호
   public void setEndPageNumPerBlock() {
      this.endPageNumPerBlock = this.blockOfCurrentPage * this.pageCntPerBlock;
      
      if (this.endPageNumPerBlock > this.totalPageCnt) {
         this.endPageNumPerBlock = this.totalPageCnt;
      }
   }
   
   
   public void setBoardList(List<HBoard> boardList) {
      this.boardList = boardList;
   }
   
   
   
   
   

}
