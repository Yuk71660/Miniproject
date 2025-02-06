package com.miniproject.controller.board;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.miniproject.model.HBoard;
import com.miniproject.model.HBoardDTO;
import com.miniproject.service.board.HBoardService;

@Controller
@RequestMapping("/hboard")
@ControllerAdvice
public class HBoardController {

	private static Logger logger = LoggerFactory.getLogger(HBoardController.class);

	@Autowired
	private HBoardService service;

	@GetMapping("/listAll")
	@ExceptionHandler(Exception.class)
	public String listAll(Model model) {
		logger.info("게시글 전체 목록 가져오기");

		List<HBoard> list;
		try {
			list = service.getEntireHBoard();
			model.addAttribute("hboardList", list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return "hboard/listAll";
	}
	
	@GetMapping("/write")
	public String showBoardWriteForm(@ModelAttribute HBoardDTO newBoard) {
	      logger.info("게시글을 저장하자.. : " + newBoard.toString());
		return "hboard/write";
	}

}
