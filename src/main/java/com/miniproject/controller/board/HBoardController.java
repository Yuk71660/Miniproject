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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.miniproject.model.BoardDetailInfo;
import com.miniproject.model.BoardUpFilesVODTO;
import com.miniproject.model.FileStatus;
import com.miniproject.model.HBoard;
import com.miniproject.model.HBoardDTO;
import com.miniproject.model.MyResponseWithoutData;
import com.miniproject.service.board.HBoardService;
import com.miniproject.util.FileProcess;
import com.miniproject.util.GetClientIPAddr;

import lombok.RequiredArgsConstructor;

// 컨트롤러 단에서 해야 할 일
// 1) request, response  처리
// 2) 최종 DAO까지 가야 하는 데이터를 처리하여 서비스단으로 전송
// 3) 서비스단을 거쳐 DAO단까지 다녀온 후 view, json 응답처리

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

   // 게시글 수정시 업로한 파일 객체들을 임식로 저장
   private List<BoardUpFilesVODTO> modifyFileList;

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
         if (service.saveBoard(newBoard, fileList)) {
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
   // ResponseEntity : 통신상태를 파악할 수 있는 HttpStatus(enum)값과 내가 전송할 객체(제네릭을 사용)를 함께 응답할
   // 수 있는 객체
   @RequestMapping(value = "/upfiles", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
   public ResponseEntity<MyResponseWithoutData> saveUploadFile(@RequestParam("file") MultipartFile file,
         @RequestParam("status") String status, HttpServletRequest request) throws InterruptedException {
      logger.info("status : " + status); // status : modify (게시글 수정에서 새로 첨부된 파일)
      logger.info("업로드된 파일의 이름 : " + file.getOriginalFilename());
      logger.info("업로드된 파일의 타입 : " + file.getContentType());
      logger.info("업로드된 파일의 사이즈 : " + file.getSize());

      ResponseEntity<MyResponseWithoutData> result = null;

      // file을 웹 서버의 하드디스크에 저장

      try {
         BoardUpFilesVODTO upFile = fp.saveFileToRealPath(file, request, "/resources/boardUpFiles");
         if (status.equals("modify")) { // 게시글 수정페이지에서 새로 첨부된 파일
            upFile.setStatus(FileStatus.NEW);
            this.modifyFileList.add(upFile);
            Thread.sleep(1000);
//            outputFileList(this.modifyFileList);
         } else if (status.equals("write")) { // 게시글 최초 작성시
            this.fileList.add(upFile);
            Thread.sleep(1000);
//            outputFileList(this.fileList);
         }

         result = new ResponseEntity<MyResponseWithoutData>(MyResponseWithoutData.successResponse(), HttpStatus.OK);

      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();

         result = new ResponseEntity<MyResponseWithoutData>(MyResponseWithoutData.failureResponse(),
               HttpStatus.INTERNAL_SERVER_ERROR);
      }

      

      return result;

   }

   @PostMapping(value = "/remFile", produces = "application/json; charset=UTF-8")
   public ResponseEntity<MyResponseWithoutData> removeUpFile(@RequestParam("removeFileName") String removeFileName, 
         @RequestParam("status") String status) {
      logger.info(removeFileName + "를 삭제하자!");
      logger.info(status);

      ResponseEntity<MyResponseWithoutData> result = null;

      // ConcurrentModificationException(컬렉션을 반복 할 때 컬렉션의 size를 수정하면 동시 수정을 감지한 메서드에서
      // 해당 수정이 허용되지 않는 경우)
      // 위 예외를 발생 시키지 않기 위해서 반복문 후에 삭제 처리
      int removedIndex = -1;
      
      if (status.equals("write")) {
         for (int i = 0; i < this.fileList.size(); i++) {
            if (this.fileList.get(i).getOriginalFileName().equals(removeFileName)) {
               fp.removeFile(this.fileList.get(i));
               removedIndex = i;
            }
         }

         this.fileList.remove(removedIndex);
//         outputFileList(this.fileList);
      } else if (status.equals("modify")) {
         
         for (int i = 0; i < this.modifyFileList.size(); i++) {
            if (this.modifyFileList.get(i).getOriginalFileName().equals(removeFileName)) {
               fp.removeFile(this.modifyFileList.get(i));
               removedIndex = i;
            }
         }

         this.modifyFileList.remove(removedIndex);
//         outputFileList(this.modifyFileList);
      }
      
      
      result = new ResponseEntity<MyResponseWithoutData>(MyResponseWithoutData.successResponse(), HttpStatus.OK);

      

      return result;
   }

   @PostMapping(value = "/removeAllFile", produces = "application/json; charset=UTF-8")
   public ResponseEntity<MyResponseWithoutData> removeAllFile() {

      ResponseEntity<MyResponseWithoutData> result = null;
      for (BoardUpFilesVODTO f : this.fileList) {
         fp.removeFile(f);
      }

      this.fileList.clear(); // 파일리스트에서도 비워줘야 한다.

//      outputFileList(this.fileList);

      result = new ResponseEntity<MyResponseWithoutData>(MyResponseWithoutData.successResponse(), HttpStatus.OK);
      return result;
   }

   @GetMapping(value = { "/viewBoard", "/modifyBoard", "/removeBoard" }) // 아래의 메서드는 "/viewBoard", "/modifyBoard" 에 매핑
   public String viewBoard(@RequestParam("boardNo") int boardNo, Model model, HttpServletRequest req) {
      logger.info(boardNo + "번 글을 조회하자!");
      logger.info("유저의 ip주소 : " + GetClientIPAddr.getClientIp(req));

      // URI에 따라 response되는 페이지가 다르기 때문에
      String returnPage = "";
      
      try {
         BoardDetailInfo bi = service.getBoardDetailInfo(boardNo, GetClientIPAddr.getClientIp(req));
         System.out.println("조회시 파일 리스트 상태 ---------------------------");
         for (BoardUpFilesVODTO f : bi.getFileList()) {
            System.out.println(f.toString());
         }
         this.modifyFileList = bi.getFileList(); // 게시글 수정시 파일 수정을 위해 담아둠
         model.addAttribute("boardDetailInfo", bi);

      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         returnPage = "redirect:./listAll?status=readFail";
      }

      
      if (req.getRequestURI().contains("view")) {
         System.out.println("조회하러 왓다");
         returnPage = "hboard/viewBoard";
      } else if (req.getRequestURI().contains("modify")) {
         System.out.println("수정하러 왓다");

         returnPage = "hboard/modifyBoardForm";
      } else if (req.getRequestURI().contains("remove")) {  // 삭제
         // 첨부 파일을 웹 서버의 하드디스크에서 삭제
         for (BoardUpFilesVODTO f : this.modifyFileList) {
            fp.removeFile(f);
         }
         // DB에서의 삭제 처리
         try {
            if (service.removeBoardProcess(boardNo)) {
               returnPage = "redirect:./listAll?status=removeSuccess";
            }
         } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            returnPage = "redirect:./viewBoard?boardNo=" + boardNo;
         }
      }

      return returnPage;
   }

   @GetMapping(value = "/showReplyForm")
   public String showReplyForm() {
      return "hboard/showReplyForm";
   }

   @PostMapping(value = "/saveReply")
   public String saveReply(HBoardDTO newReply) {

      String returnPage = "redirect:./listAll";
      logger.info(newReply.toString() + "를 저장하자!");
      try {
         service.saveReply(newReply);
         returnPage += "?status=success";
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         returnPage += "?status=fail";

      }

      return returnPage;
   }

   @PostMapping("/modifyUpdateStatus")
   public ResponseEntity<MyResponseWithoutData> modifyUpdateStatus(
         @RequestParam("deletedFileName") String deletedFileName, @RequestParam("status") String status) {
      logger.info(deletedFileName + "파일을 " + status + "상태로 변경");

      for (BoardUpFilesVODTO f : this.modifyFileList) {
         if (status.equals("delete") && f.getOriginalFileName().equals(deletedFileName)) { // 파일을 삭제 될 상태로 변경
            f.setStatus(FileStatus.DELETE);
         } else if (status.equals("cancel") && f.getOriginalFileName().equals(deletedFileName)) { // 파일 삭제 취소 상태로 변경
            f.setStatus(null);
         }
      }

//      outputFileList(this.modifyFileList);

      return new ResponseEntity<MyResponseWithoutData>(MyResponseWithoutData.successResponse(), HttpStatus.OK);
   }
   
   @PostMapping("/modifyCancel") 
   public ResponseEntity<MyResponseWithoutData> modifyCancel() {
      for (BoardUpFilesVODTO f : this.modifyFileList) {
         if (f.getStatus() == FileStatus.DELETE) {
            f.setStatus(null);
         } else if (f.getStatus() == FileStatus.NEW) {
            // 삭제
            fp.removeFile(f);
         }
      }
      
      this.modifyFileList.clear();
      
      return new ResponseEntity<MyResponseWithoutData>(MyResponseWithoutData.successResponse(), HttpStatus.OK);
   }
   
   @PostMapping("/modifyBoard")
   public String modifyBoardPost(HBoardDTO modifyBoard) {
      logger.info(modifyBoard + "를 수정하자");
      System.out.println("====================================================================================");
      System.out.println(" 수정전 파일 리스트  : " + this.modifyFileList.size());
      System.out.println("====================================================================================");
      for (BoardUpFilesVODTO f : this.modifyFileList) {
         System.out.println(f.toString());
      }
      
      String returnPage = "redirect:./viewBoard?boardNo=" + modifyBoard.getBoardNo();
      
      try {
         if (service.modifyBoard(modifyBoard, this.modifyFileList)) {
            returnPage += "&status=modifySuccess";
         }
         
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         
         returnPage += "&status=modifyFail";
      }
      
      postFileProcess();
      
      return returnPage;
      
   }
   
//   @GetMapping("/removeBoard")
//   public void removeBoard(@RequestParam("boardNo") int boardNo) {
//      logger.info(boardNo + "번 글 삭제!");
//      
//   }

   private void postFileProcess() {
      for (BoardUpFilesVODTO f : this.modifyFileList) {
         if (f.getStatus() == FileStatus.DELETE) {
            fp.removeFile(f);
         }
      }
      
      this.modifyFileList.clear();
   }

   private void outputFileList(List<BoardUpFilesVODTO> list) {
      System.out.println("====================================================================================");
      System.out.println(" 현재 업로드된 파일 리스트  : " + list.size());
      System.out.println("====================================================================================");
      for (BoardUpFilesVODTO f : list) {
         System.out.println(f.toString());
      }
   }
   
   
}
