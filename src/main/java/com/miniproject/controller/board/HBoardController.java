package com.miniproject.controller.board;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.miniproject.model.BoardUpFilesVODTO;
import com.miniproject.model.HBoard;
import com.miniproject.model.HBoardDTO;
import com.miniproject.service.board.HBoardService;
import com.miniproject.util.FileProcess;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/hboard")
@ControllerAdvice
@RequiredArgsConstructor
public class HBoardController {

	private static Logger logger = LoggerFactory.getLogger(HBoardController.class);
	
	private final HBoardService service;
	
	private final FileProcess fp;

	private List<BoardUpFilesVODTO> fileList = new ArrayList<BoardUpFilesVODTO>();

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
	public String showBoardWriteForm() {
		return "hboard/write";
	}

	@PostMapping("/write")
	public String saveHBoard(@ModelAttribute HBoardDTO newBoard) {
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
	
	// MultipartFile : front에서 전송된 이진 데이터 파일을 저장하는 객체
	   @PostMapping("/upfiles")
	   public void saveUploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
	      logger.info("업로드된 파일의 이름 : " + file.getOriginalFilename());
	      logger.info("업로드된 파일의 타입 : " + file.getContentType());
	      logger.info("업로드된 파일의 사이즈 : " + file.getSize());
	      
	      // file을 웹 서버의 하드디스크에 저장
	      try {
	         BoardUpFilesVODTO upFile =  fp.saveFileToRealPath(file, request);
	         this.fileList.add(upFile);   
	      } catch (IOException e) {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	      }
	      
	      outputFileList();
	      
	   }

	   private void outputFileList() {
	      System.out.println("====================================================================================");
	      System.out.println(" 현재 업로드된 파일 리스트 ");
	      System.out.println("====================================================================================");
	      for (BoardUpFilesVODTO f :fileList) {
	         System.out.println(f.toString());
	      }
	   }

}
