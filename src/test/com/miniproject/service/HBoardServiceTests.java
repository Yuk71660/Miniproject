package com.miniproject.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.miniproject.model.BoardUpFilesVODTO;
import com.miniproject.model.HBoardDTO;
import com.miniproject.model.PageRequestDTO;

import java.util.ArrayList;
import java.util.List;
import com.miniproject.service.board.HBoardService;

@RunWith(SpringJUnit4ClassRunner.class) // 아래의 객체가 Junit4 클래스와 함께 동작하도록
@ContextConfiguration( // 설정 파일의 위치 (여기에서는 dataSource객체가 생성된 root-context.xml의 위치)
      locations = { "file:src/main/webapp/WEB-INF/spring/**/root-context.xml" })
public class HBoardServiceTests {

   @Autowired
   private HBoardService service;

   @Test
   public void testGetEntireHBoard() throws Exception {
      System.out.println(service.getEntireHBoard(PageRequestDTO.builder().pageNo(2)
            .rowCntPerPage(40).build()));
      
   }

//   @Test
   public void testSaveBoard_insertDummies() throws Exception {
      for (int i = 0; i < 100; i++) {
         int randomNum = (int) Math.floor(Math.random() * 4);
         String[] members = { "abc123", "admin", "dooly", "subAdmin" };

         HBoardDTO newBoard = HBoardDTO.builder().title("dummy board... title" + i)
               .content("dummy board...content" + i).writer(members[randomNum]).build();

         List<BoardUpFilesVODTO> lst = new ArrayList<BoardUpFilesVODTO>();

//         if (service.saveBoard(newBoard, lst)) {
//            System.out.println("테스트 성공");
//         } else {
//            System.out.println("테스트 실패");
//         }

         service.saveBoard(newBoard, lst);

      }
   }
}
