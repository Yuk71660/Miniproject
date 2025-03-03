package com.miniproject.controller.board;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.miniproject.model.BoardDetailInfo;
import com.miniproject.model.PageRequestDTO;
import com.miniproject.model.PageResponseDTO;
import com.miniproject.model.RBoard;
import com.miniproject.model.RBoardDTO;
import com.miniproject.service.board.RBoardService;
import com.miniproject.util.GetClientIPAddr;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/rboard")
@RequiredArgsConstructor
public class RBoardController {
   private static Logger logger = LoggerFactory.getLogger(RBoardController.class);
   
   private final RBoardService service;
   
   @GetMapping("/listAll")
   public String listAll(Model model, PageRequestDTO pageRequestDTO) {
      logger.info("게시글 전체 목록을 가져오자....");
      
      logger.info("pageRequestDTO : " + pageRequestDTO);

      String resultPage = "rboard/listAll";

      PageResponseDTO<RBoard> pageResponseDTO;
      try {
         pageResponseDTO = service.getEntireHBoard(pageRequestDTO);
         model.addAttribute("pageResponseDTO", pageResponseDTO);
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();

      }

      return resultPage;
   }
   
   @GetMapping("/write")
   public String showBoardWriteForm() {
      return "rboard/write";
   }
   
   @PostMapping("/write")
   public String saveHBoard(@ModelAttribute RBoardDTO newBoard) {
      logger.info("게시글을 저장하자.. : " + newBoard.toString());

      String returnPage = "redirect:./listAll";


      try {
         if (service.saveBoard(newBoard)) {
            returnPage += "?status=success";
         }
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         returnPage += "?status=fail";
      }

      return returnPage;
   }
   
   @GetMapping(value = { "/viewBoard", "/modifyBoard"}) // 아래의 메서드는 "/viewBoard", "/modifyBoard" 에 매핑
   public String viewBoard(@RequestParam("boardNo") int boardNo, Model model, HttpServletRequest req) {
      logger.info(boardNo + "번 글을 조회하자!");
      logger.info("유저의 ip주소 : " + GetClientIPAddr.getClientIp(req));

      // URI에 따라 response되는 페이지가 다르기 때문에
      String returnPage = "";

      try {
         BoardDetailInfo bi = service.getBoardDetailInfo(boardNo, GetClientIPAddr.getClientIp(req));
         model.addAttribute("boardDetailInfo", bi);

      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         returnPage = "redirect:./listAll?status=readFail";
      }

      if (req.getRequestURI().contains("view")) {
         System.out.println("조회하러 왓다");
         returnPage = "rboard/viewBoard";
      } else if (req.getRequestURI().contains("modify")) {
         System.out.println("수정하러 왓다");

         returnPage = "rboard/modifyBoardForm";
      } 

      return returnPage;
   }
   
   @PostMapping("/modifyBoard")
   public String modifyBoardPost(RBoardDTO modifyBoard) {
      logger.info(modifyBoard + "를 수정하자");

      String returnPage = "redirect:./viewBoard?boardNo=" + modifyBoard.getBoardNo();

      try {
         if (service.modifyBoard(modifyBoard)) {
            returnPage += "&status=modifySuccess";
         }

      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();

         returnPage += "&status=modifyFail";
      }

      return returnPage;

   }
   
   @GetMapping("/removeBoard")
   public String removeBoard(@RequestParam("boardNo") int boardNo) {
      logger.info(boardNo + "를 삭제하자");
      String returnPage = "";
      try {
         if(service.removeBoard(boardNo)) {
            returnPage = "redirect:./listAll?status=success";
         }
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         returnPage = "redirect:./viewBoard?boardNo=" + boardNo + "&status=fail";
      }
      
      return returnPage;
   }
}
