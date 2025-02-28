package com.miniproject.dao.board;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.miniproject.model.PageRequestDTO;
import com.miniproject.model.PageResponseDTO;
import com.miniproject.model.RBoard;

@Repository
public class RBoardDAOImpl implements RBoardDAO {
   

   // SqlSessionTemplate 객체 주입
   @Autowired
   private SqlSession ses;

   private static final String NS = "com.miniproject.mapper.rboardmapper";

   @Override
   public List<RBoard> selectAllHBoard(PageResponseDTO<RBoard> pageResponseDTO) throws Exception {
      // TODO Auto-generated method stub
      return ses.selectList(NS + ".getEntireRBoard", pageResponseDTO);
   }

   @Override
   public int getTotalCountRow() throws Exception {
      
      return ses.selectOne(NS + ".getTotalCountRow");
   }


   @Override
   public int getSearchResultRowCount(PageRequestDTO pageRequestDTO) throws Exception {
      pageRequestDTO.setSearchWord("%" + pageRequestDTO.getSearchWord() + "%");  // like 검색을 위해
      return ses.selectOne(NS + ".getSearchResultCountRow", pageRequestDTO);
   }

}
