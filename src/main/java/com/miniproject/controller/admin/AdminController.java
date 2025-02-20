package com.miniproject.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
   
   @GetMapping("/")
   public String home() {
      System.out.println("어드민 홈 호출!!!!!");
      return "admin/adminHome";
   }

}
