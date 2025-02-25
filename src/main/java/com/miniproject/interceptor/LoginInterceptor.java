package com.miniproject.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginInterceptor extends HandlerInterceptorAdapter {
   private static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

   @Override
   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
         throws Exception {

      boolean isShowLoginPage = false;

      logger.info("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★ 로그인 이전 preHandle() ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★ ");

      isShowLoginPage = true;

      return isShowLoginPage;
   }

   @Override
   public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
         ModelAndView modelAndView) throws Exception {

      if (request.getMethod().toLowerCase().equals("post")) {
         logger.info(
               "★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★ 로그인 이후 postHandle() ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★ ");
         
         // 로그인 성공 했다면 
         //  1) 로그인 유저의 정보를 세션에 바인딩
         //  2) 세션에 destUrl 이 있다면 그쪽으로 이동, 없다면 "/"로 이동
         
         // 로그인 실패 했다면
         // 로그인 페이지로 이동
      }
      super.postHandle(request, response, handler, modelAndView);
   }

}
