package com.miniproject.controller.member;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.miniproject.model.MemberDTO;
import com.miniproject.model.MyResponseWithoutData;
import com.miniproject.service.member.MemberService;
import com.miniproject.util.FileProcess;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

	private static Logger logger = LoggerFactory.getLogger(MemberController.class);

	private final FileProcess fp;
	   
	private final MemberService service;

	@GetMapping("/register")
	private String showRegisterForm() {
		return "/member/register";
	}

	@PostMapping("/saveMember")
	public void saveMember(MemberDTO newMember, @RequestParam("userProfile") MultipartFile userImg,
			HttpServletRequest req) {
		logger.info(newMember.toString() + "을 회원 가입 시키자");

		System.out.println(fp.hashCode());
		if (userImg.getSize() > 0) { // 유저 이미지가 있다면...
			try {
				String userImgName = fp.saveUserProfile(newMember.getUserId(), userImg, req); // userId.확장자로 파일 저장
				newMember.setUserImg("memberImg/" + userImgName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				newMember.setUserImg(null); // 유저가 올린 이미지를 저장 실패 했을 경우.. default이미지가 저장되도록...

			}
		}

		logger.info(newMember.toString() + "을 회원 가입 시키자");
	}

	@PostMapping(value = "/duplicateId", produces = "application/json; charset=utf-8")
	public ResponseEntity<MyResponseWithoutData> duplicateId(@RequestParam("userId") String userId) {
		logger.info(userId + "가 중복되는지 검사");

		ResponseEntity<MyResponseWithoutData> result = null;

		try {
			if (service.isUserIdDuplicate(userId)) {
				// 중복
				result = new ResponseEntity<MyResponseWithoutData>(new MyResponseWithoutData(409, "Duplicate"),
						HttpStatus.OK);
			} else {
				// 중복 x
				result = new ResponseEntity<MyResponseWithoutData>(new MyResponseWithoutData(200, "Available"),
						HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return result;

	}

	@PostMapping("/sendAuthCode")
	public ResponseEntity<MyResponseWithoutData> sendAuthCode(@RequestParam("emailAddr") String emailAddr,
			HttpSession session) {

		ResponseEntity<MyResponseWithoutData> result = null;

		String authCode = UUID.randomUUID().toString();
		session.setAttribute("authCode", authCode); // 인증 코드를 세션 객체에 바인딩

		logger.info(emailAddr + "로 인증코드 보내자 : " + authCode);

		try {
//			new SendMailService(emailAddr, authCode).send();

			result = new ResponseEntity<MyResponseWithoutData>(MyResponseWithoutData.successResponse(), HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = new ResponseEntity<MyResponseWithoutData>(MyResponseWithoutData.failureResponse(), HttpStatus.OK);
		}

		return result;
	}

	@PostMapping("/confirmAuthCode")
	public ResponseEntity<MyResponseWithoutData> confirmAuthCode(
			@RequestParam("confirmCodeInput") String confirmCodeInput, HttpSession ses) {
		ResponseEntity<MyResponseWithoutData> result = null;

		// confirmCodeInput와 세션에 바인딩한 authCode 값을 비교
		String authCode = (String) ses.getAttribute("authCode");

		if (confirmCodeInput.equals(authCode)) {
			result = new ResponseEntity<MyResponseWithoutData>(MyResponseWithoutData.successResponse(), HttpStatus.OK);
		} else {
			result = new ResponseEntity<MyResponseWithoutData>(MyResponseWithoutData.failureResponse(), HttpStatus.OK);
		}

		return result;

	}

	@PostMapping("/invalidAuthCode")
	public ResponseEntity<MyResponseWithoutData> removeAuthCode(HttpSession ses) {
		ResponseEntity<MyResponseWithoutData> result = null;

		if (ses.getAttribute("authCode") != null) {
			ses.removeAttribute("authCode");
		}

		result = new ResponseEntity<MyResponseWithoutData>(MyResponseWithoutData.successResponse(), HttpStatus.OK);
		return result;

	}
}
