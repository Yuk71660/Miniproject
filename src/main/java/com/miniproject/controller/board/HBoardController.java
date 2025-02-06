package com.miniproject.controller.board;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.miniproject.model.Hboard;
import com.miniproject.service.board.HBoardService;

@Controller
@RequestMapping("/hboard")
public class HBoardController {

	private static Logger logger = LoggerFactory.getLogger(HBoardController.class);

	@Autowired
	private HBoardService service;

	@GetMapping("/listAll")
	public String listAll(Model model) {
		logger.info("게시글 전체 목록 가져오기");

		List<Hboard> list = service.getEntireHBoard();

		model.addAttribute("hboardList", list);

		return "/hboard/listAll";
	}

}
