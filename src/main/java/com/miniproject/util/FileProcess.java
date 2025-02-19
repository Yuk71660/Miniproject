package com.miniproject.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Mode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.miniproject.model.BoardUpFilesVODTO;

/**
 * @author Administrator
 * @date 2025. 2. 10.
 * @packagename com.miniproject.util
 * @typeName FileProcess
 * @todo 업로드된 파일을 웹 서버의 특정 경로에 저장, 업로드 된 파일의 삭제
 * 
 */
@Component // 스프링의 빈으로 등록 되도록 하는 어노테이션
public class FileProcess {

   private String realPath;
   private String saveFilePath;
   private String os;

   private Logger logger = LoggerFactory.getLogger(getClass());

   public FileProcess() {
      this.os = System.getProperty("os.name").toLowerCase();
   }

   /**
    * @author Administrator
    * @data 2025. 2. 12.
    * @enclosing_method removeFile
    * @todo 업로드된 파일 객체를 받아 웹서버에서 삭제한다.
    * @param BoardUpFilesVODTO removeFile : 삭제할 파일 객체
    * @returnType void
    */
   public void removeFile(BoardUpFilesVODTO removeFile) {
      String tmpNewFileName = null;
      String tmpThubFileName = null;

      // 현재 시스템의 os가 windows냐 linux냐에 따라...
      if (this.os.contains("windows")) {
         tmpNewFileName = removeFile.getNewFileName().replace("/", "\\");
         tmpThubFileName = removeFile.getThumbFileName().replace("/", "\\");
      } else if (this.os.contains("linux")) {
         tmpNewFileName = removeFile.getNewFileName();
         tmpThubFileName = removeFile.getThumbFileName();
      }

      // 이미지파일 -> newFileName 경로의 파일 삭제 + thumbFileName 경로의 파일삭제

      File tmp = new File(this.realPath + tmpNewFileName);
      tmp.delete();

      if (ImageMimeType.isImage(removeFile.getExt())) {
         File tmpThumb = new File(this.realPath + tmpThubFileName);
         tmpThumb.delete();

      }
   }

   public String saveUserProfile(String userId, MultipartFile userImg, HttpServletRequest req) throws IOException {
      String originalFileName = userImg.getOriginalFilename();
      String ext = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
      
      this.realPath = req.getSession().getServletContext().getRealPath("/resources/memberImg");
      logger.info("파일이 저장될 웹 서버의 실제 경로 : " + this.realPath);
      
      String fileName = userId + "." + ext;
      File saveUserImgFile = new File(realPath + File.separator + fileName);
      FileUtils.writeByteArrayToFile(saveUserImgFile, userImg.getBytes());
      
      return fileName;
   }

   /**
    * @author Administrator
    * @data 2025. 2. 10.
    * @enclosing_method saveFileToRealPath
    * @todo : 업로드된 파일을 실제 물리경로(realPath)에 저장
    * @param : MultipartFile file - 유저가 업로드 한 파일
    * @param : HttpServletRequest request
    * @param : String saveFileDir - 업로드한 파일이 저장되는 sub경로
    * @throws IOException
    * @returnType BoardUpFilesVODTO
    */
   public BoardUpFilesVODTO saveFileToRealPath(MultipartFile file, HttpServletRequest request, String saveFileDir)
         throws IOException {
      BoardUpFilesVODTO result = null;

      String originalFileName = file.getOriginalFilename();
      String fileType = file.getContentType();
      String ext = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
      long size = file.getSize();
      String thumbFileName = null;
      String base64Image = null;

//      System.out.println(Arrays.toString(file.getBytes()));

      String newFileName = null;

      this.realPath = request.getSession().getServletContext().getRealPath(saveFileDir);
      logger.info("파일이 저장될 웹 서버의 실제 경로 : " + this.realPath);

      String[] ymd = makeCalendarPath();
      makeDirectory(ymd);
      this.saveFilePath = realPath + ymd[ymd.length - 1]; // realPath + "\2025\02\10\"

      if (size > 0) {

         newFileName = renameUniqueFileName(originalFileName); // UUID를 이용해 중복되지 않을 값으로 처리

         File saveFile = new File(saveFilePath + File.separator + newFileName);

         FileUtils.writeByteArrayToFile(saveFile, file.getBytes()); // 실제 파일 저장

         // 이미지 파일이냐?
         if (ImageMimeType.isImage(ext)) {
            // -> 썸네일 이미지를 만들어 저장
            thumbFileName = makeThumbnailImage(newFileName);
//            logger.info("썸네일 이미지 파일 이름 : " + thumbFileName);

            // base64문자열 만들어 저장
            base64Image = makeBase64Encoding(thumbFileName);
//            logger.info("base64 문자열" + base64Image);
            // 과제(?) : base64 문자열로 디코딩하여 파일로 저장해보기

         }

         result = BoardUpFilesVODTO.builder().originalFileName(originalFileName)
               .newFileName(ymd[ymd.length - 1].replace("\\", "/") + "/" + newFileName)
               .thumbFileName(ymd[ymd.length - 1].replace("\\", "/") + "/" + thumbFileName).fileType(fileType)
               .ext(ext).size(size).base64Image(base64Image).build();

         logger.info("업로드 된 파일의 정보 : " + result);

      }

      return result;
   }

   /**
    * @author Administrator
    * @data 2025. 2. 11.
    * @enclosing_method makeBase64Encoding
    * @todo 주어진 썸네일 이미지 파일을 대상으로 Base64인코딩 문자열을 만들어 반환
    * @param
    * @throws IOException
    * @returnType String : base64로 인코딩된 문자열
    */
   private String makeBase64Encoding(String thumbFileName) throws IOException {
      // Base64인코딩 : 이진 데이터를 text로 만드는 인코딩의 한가지
      // 특징 : 별도 파일을 저장할 공간이 필요하지 않다.(장점), 실제 파일을 저장하는 것보다 더 크기가 클 수 있다(단점)
      // 특징 : 인코딩에 따른 부하가 있다.

      String result = null;
      File thumb = new File(this.saveFilePath + File.separator + thumbFileName);

      byte[] thumbFile = FileUtils.readFileToByteArray(thumb);

      return Base64.getEncoder().encodeToString(thumbFile);
   }

   /**
    * @author Administrator
    * @data 2025. 2. 11.
    * @enclosing_method makeThumbnailImage
    * @todo imgscalr라이브러리를 이용해 newFileName의 이미지를 읽어와, 사이즈를 줄이고 새로운 파일이름으로 저장
    * @param
    * @throws IOException
    * @returnType thumbNailImageFileName : 저장된 썸네일 파일 이름
    */
   private String makeThumbnailImage(String newFileName) throws IOException {
      System.out.println(newFileName);
      BufferedImage originalImage = ImageIO.read(new File(this.saveFilePath + File.separator + newFileName));
      BufferedImage thumbnail = Scalr.resize(originalImage, Mode.FIT_TO_WIDTH, 50);

      String thumbNailImageFileName = "thumb_" + newFileName; // 새롭게 저장될 썸네일 이미지 파일이름

      File saveThumbImg = new File(this.saveFilePath + File.separator + thumbNailImageFileName);
      String ext = newFileName.substring(newFileName.lastIndexOf(".") + 1);

      ImageIO.write(thumbnail, ext, saveThumbImg); // 파일저장

      return thumbNailImageFileName;

   }

   /**
    * @author Administrator
    * @data 2025. 2. 10.
    * @enclosing_method renameUniqueFileName
    * @todo TODO
    * @param
    * @returnType String
    */
   private String renameUniqueFileName(String originalFileName) {
      UUID uuid = UUID.randomUUID();
      String uniqueFileName = uuid.toString() + "_" + originalFileName;
      logger.info("우주적으로 유니크한 새 파일 이름 : " + uniqueFileName);

      return uuid.toString() + "_" + originalFileName;
   }

   /**
    * @author Administrator
    * @data 2025. 2. 10.
    * @enclosing_method renameFileName
    * @todo "같은이름의 파일(파일숫자 + 1).확장자"로 저장
    * @param
    * @returnType String
    */
   private String renameFileName(String originalFileName, String ext) {
      String tmpNewFileName = originalFileName;
      int cnt = 1;
      while (checkFileExist(tmpNewFileName)) {

         String fileNameWithoutExt = originalFileName.substring(0, originalFileName.lastIndexOf("."));
         int startIndex = tmpNewFileName.indexOf("(");
         if (startIndex != -1) {
            String preStartIndex = tmpNewFileName.substring(0, startIndex);
            tmpNewFileName = preStartIndex + "(" + cnt + ")" + "." + ext;
         } else {
            tmpNewFileName = fileNameWithoutExt + "(" + cnt + ")" + "." + ext;
         }
         cnt++;
      }

      logger.info("새로운 파일 이름 : " + tmpNewFileName);
      return tmpNewFileName;
   }

   /**
    * @author Administrator
    * @data 2025. 2. 10.
    * @enclosing_method checkFileExist
    * @todo : 파일 저장경로(saveFilePath)에 originalFileName이 있는지 없는지 검사
    * @param
    * @returnType 파일이 중복되면 true, 중복되지 않으면 false 반환
    */
   private boolean checkFileExist(String originalFileName) {
      File tmp = new File(this.saveFilePath);
      boolean isFind = false;

      for (String f : tmp.list()) {
         if (f.equals(originalFileName)) {
            isFind = true;
            System.out.println("파일이름이 중복됨!!!!!!!!!!");
            break;
         }
      }

      return isFind;
   }

   /**
    * @author Administrator
    * @data 2025. 2. 10.
    * @enclosing_method makeDirectory
    * @todo 넘겨받은 ymd배열의 구조로 realPath 하위에 폴더를 생성
    * @param String[] ymd = {year, month, date}
    * @returnType void
    */
   private void makeDirectory(String[] ymd) {
      if (!new File(this.realPath + ymd[ymd.length - 1]).exists()) {
         // 경로가 존재하지 않으므로 디렉토리 생성
         for (String path : ymd) {
            File tmp = new File(this.realPath + path);
            if (!tmp.exists()) {
               tmp.mkdir(); // 디렉토리 생성
            }
         }
      }
   }

   /**
    * @author Administrator
    * @data 2025. 2. 10.
    * @enclosing_method makeCalendarPath
    * @todo 현재 년월일을 얻어와 realPath하위에 "년/월/일" 폴더로 만들기 위해 디렉토리 구조를 먼저 만듦
    * @param
    * @returnType String[] ymd = {year, month, date}
    */
   private String[] makeCalendarPath() {
      Calendar cal = Calendar.getInstance();
      String year = File.separator + cal.get(Calendar.YEAR) + ""; // "\2025"
      String month = year + File.separator + new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1); // "\2025\02"
      String date = month + File.separator + new DecimalFormat("00").format(cal.get(Calendar.DATE)); // "\2025\02\10"

      logger.info("파일이 저장될 현재 날짜 기반의 폴더 : " + year + ", " + month + ", " + date);

      String[] ymd = { year, month, date };

      return ymd;
   }

}
