package com.miniproject.controller.member;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

	private static Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@GetMapping("/register")
	private String showRegisterForm() {
		return "/member/register";
	}
}
