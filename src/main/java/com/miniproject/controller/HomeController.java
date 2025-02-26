package com.miniproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
		
		return "index";
	}
	
	@RequestMapping(value = "/interceptorA")
	public void interceptorA() {
		
		System.out.println("인터셉트해옴!");
		
	}
	
	@GetMapping("/blog")
	public void showBlog() {
		
	}
	
	@PostMapping("/blog")
	public void searchBlog(@RequestParam("keyWord") String keyWord) {
		System.out.println(keyWord + "를 검색");
		
	}
}
