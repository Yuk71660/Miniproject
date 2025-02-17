package com.miniproject.controller.member;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.miniproject.model.MemberDTO;
import com.miniproject.model.MyResponseWithoutData;
import com.miniproject.service.member.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

	private static Logger logger = LoggerFactory.getLogger(MemberController.class);

	private final MemberService service;

	@GetMapping("/register")
	private String showRegisterForm() {
		return "/member/register";
	}

	@PostMapping("/saveMember")
	public void saveMember(MemberDTO newMember, @RequestParam("userProfile") MultipartFile userImg) {
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
}
