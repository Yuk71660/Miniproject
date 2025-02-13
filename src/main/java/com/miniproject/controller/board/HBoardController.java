package com.miniproject.controller.board;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.miniproject.model.BoardUpFilesVODTO;
import com.miniproject.model.HBoard;
import com.miniproject.model.HBoardDTO;
import com.miniproject.model.MyResponseWithoutData;
import com.miniproject.service.board.HBoardService;
import com.miniproject.util.FileProcess;

import lombok.RequiredArgsConstructor;

// 컨트롤러 단에서 해야 할 일
// 1) request, response  처리


@Controller
@RequestMapping("/hboard")
@RequiredArgsConstructor
public class HBoardController {
   // HBoardController.class에서 사용할 Logger 객체를 얻어옴
   private static Logger logger = LoggerFactory.getLogger(HBoardController.class);

   private final HBoardService service;

   private final FileProcess fp;
   
   // 게시글 작성시 업로드한 파일객체들을 임시로 저장
   private List<BoardUpFilesVODTO> fileList = new ArrayList<BoardUpFilesVODTO>();
   
   
   @GetMapping("/listAll")
   public String listAll(Model model) {
      logger.info("게시글 전체 목록을 가져오자....");
      
      String resultPage = "hboard/listAll";
      
      List<HBoard> list;
      try {
         list = service.getEntireHBoard();
         model.addAttribute("hboardList", list);
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         

      }

      return resultPage;
   }
   
   @GetMapping("/write")
   public String showBoardWriteForm() {
      return "hboard/write";
   }
   
   @PostMapping("/write")
   public String saveHBoard(@ModelAttribute HBoardDTO newBoard) {
      logger.info("게시글을 저장하자.. : " + newBoard.toString());
      
      String returnPage = "redirect:./listAll";
      
      // 업로드된 파일이 있다면 업로드된 파일또한 DB에 저장
      
      try {
         if(service.saveBoard(newBoard, fileList)) {
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
   // ResponseEntity : 통신상태를 파악할 수 있는 HttpStatus(enum)값과 내가 전송할 객체(제네릭을 사용)를 함께 응답할 수 있는 객체
   @RequestMapping(value="/upfiles", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
   public ResponseEntity<MyResponseWithoutData> saveUploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
      logger.info("업로드된 파일의 이름 : " + file.getOriginalFilename());
      logger.info("업로드된 파일의 타입 : " + file.getContentType());
      logger.info("업로드된 파일의 사이즈 : " + file.getSize());
      
      ResponseEntity<MyResponseWithoutData> result = null;
      
      // file을 웹 서버의 하드디스크에 저장
      
      try {
         boolean isDuplicate = false;
         BoardUpFilesVODTO upFile =  fp.saveFileToRealPath(file, request);
         for (BoardUpFilesVODTO f:this.fileList) {
            if (file.getOriginalFilename().equals(f.getOriginalFileName())) {
               isDuplicate = true;
            }
         }
         if (!isDuplicate) {
            this.fileList.add(upFile);
         }
            
         
         result = new ResponseEntity<MyResponseWithoutData>(MyResponseWithoutData.successResponse(), HttpStatus.OK);
         
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         
         result = new ResponseEntity<MyResponseWithoutData>(MyResponseWithoutData.failureResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
      }
      
      outputFileList();
      
      
      return result;
      
   }
   
   @PostMapping(value="/remFile" , produces = "application/json; charset=UTF-8")
   public ResponseEntity<MyResponseWithoutData> removeUpFile(@RequestParam("removeFileName") String removeFileName) {
      logger.info(removeFileName + "를 삭제하자!");
      
      ResponseEntity<MyResponseWithoutData> result = null;
      
      // ConcurrentModificationException(컬렉션을 반복 할 때 컬렉션의 size를 수정하면 동시 수정을 감지한 메서드에서 해당 수정이 허용되지 않는 경우) 
      // 위 예외를 발생 시키지 않기 위해서 반복문 후에 삭제 처리
      int removedIndex = -1;
      for (int i = 0; i < this.fileList.size(); i++) {
         if(this.fileList.get(i).getOriginalFileName().equals(removeFileName)) {
            fp.removeFile(this.fileList.get(i));
            removedIndex = i;
         }
      }
      
      this.fileList.remove(removedIndex);
      
      result = new ResponseEntity<MyResponseWithoutData>(MyResponseWithoutData.successResponse(), HttpStatus.OK);
      
      outputFileList();
      
      return result;
   }
   
   @PostMapping(value="/removeAllFile", produces = "application/json; charset=UTF-8")
   public ResponseEntity<MyResponseWithoutData> removeAllFile() {
      
      ResponseEntity<MyResponseWithoutData> result = null;
      for (BoardUpFilesVODTO f:this.fileList) {
         fp.removeFile(f);
      }
      
      this.fileList.clear();  // 파일리스트에서도 비워줘야 한다.
      
      outputFileList();
      
      result = new ResponseEntity<MyResponseWithoutData>(MyResponseWithoutData.successResponse(), HttpStatus.OK);
      return result;
   }
   
   @GetMapping(value = "/viewBoard")
   public void viewBoard(@RequestParam("boardNo") int boardNo) {
	   System.out.println("조회" + boardNo);
	   
	   try {
		service.getBoardDetailInfo(boardNo);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }

   private void outputFileList() {
      System.out.println("====================================================================================");
      System.out.println(" 현재 업로드된 파일 리스트  : " + fileList.size());
      System.out.println("====================================================================================");
      for (BoardUpFilesVODTO f :fileList) {
         System.out.println(f.toString());
      }
   }
}
