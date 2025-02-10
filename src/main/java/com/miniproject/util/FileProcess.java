package com.miniproject.util;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.miniproject.model.BoardUpFilesVODTO;

/**
 * @author Administrator
 * @date 2025. 2. 10.
 * @packagename com.miniproject.util
 * @typeName filProcess
 * @todo 업로드된 파일을 특정 경로에 저장
 *
 */

@Component
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
	 * @returnType BoardUpFilesVODTO
	 */
	public BoardUpFilesVODTO saveFileToRealPath(MultipartFile file, HttpServletRequest request) {
		BoardUpFilesVODTO result = null;

		String originalFileName = file.getOriginalFilename();
		String fileType = file.getContentType();
		String ext = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
		long size = file.getSize();

		this.realPath = request.getSession().getServletContext().getRealPath("/resources/boardUpFiles");
		logger.info("파일이 저장될 웹 서버의 실제 경로 : " + this.realPath);

		String[] ymd = makeCalendarPath();
		makeDirectory(ymd);
		this.saveFilePath = realPath + ymd[ymd.length - 1]; // realPath + "\2025\02\10\"

		if (size > 0) {

		}

		return null;
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
	 * @returnType void
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
