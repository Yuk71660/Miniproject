package com.miniproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
	
}
