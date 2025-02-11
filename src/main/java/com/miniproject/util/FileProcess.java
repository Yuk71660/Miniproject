package com.miniproject.util;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.Calendar;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import com.miniproject.model.BoardUpFilesVODTO;

/**
 * @author Administrator
 * @date 2025. 2. 10.
 * @packagename com.miniproject.util
 * @typeName FileProcess
 * @todo 업로드된 파일을 웹 서버의 특정 경로에 저장
 * 
 */
@Component // 스프링의 빈으로 등록 되도록 하는 어노테이션
public class FileProcess {

	private String realPath;
	private String saveFilePath;

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * @author Administrator
	 * @data 2025. 2. 10.
	 * @enclosing_method saveFileToRealPath
	 * @todo : 업로드된 파일을 실제 물리경로(realPath)에 저장
	 * @param : MultipartFile file - 유저가 업로드 한 파일
	 * @param : HttpServletRequest request
	 * @throws IOException
	 * @returnType BoardUpFilesVODTO
	 */
	public BoardUpFilesVODTO saveFileToRealPath(MultipartFile file, HttpServletRequest request) throws IOException {
		BoardUpFilesVODTO result = null;

		String originalFileName = file.getOriginalFilename();
		String fileType = file.getContentType();
		String ext = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
		long size = file.getSize();
		String newFileName = null;

		this.realPath = request.getSession().getServletContext().getRealPath("/resources/boardUpFiles");
		logger.info("파일이 저장될 웹 서버의 실제 경로 : " + this.realPath);

		String[] ymd = makeCalendarPath();
		makeDirectory(ymd);
		this.saveFilePath = realPath + ymd[ymd.length - 1]; // realPath + "\2025\02\10\"

		if (size > 0) {

			newFileName = ymd[ymd.length - 1].replace("\\", "/") + renameUniqueFileName(originalFileName); // UUID를 이용해 중복되지 않을 값으로 처리

			File saveFile = new File(saveFilePath + File.separator + newFileName);
			FileUtils.writeByteArrayToFile(saveFile, file.getBytes());

		}

		return null;
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
