package com.miniproject.controller.board;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.miniproject.model.PageRequestDTO;
import com.miniproject.model.PageResponseDTO;
import com.miniproject.model.RBoard;
import com.miniproject.service.board.RBoardService;

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
}
