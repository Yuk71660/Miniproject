package com.miniproject.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.miniproject.model.Member;

/**
 * @author Administrator
 * @date 2025. 2. 20.
 * @packagename com.miniproject.interceptor
 * @typeName AuthenticationInterceptor
 * @todo : 로그인 인증이 필요한 페이지(글작성, 글수정, 글삭제, 답(댓)글작성/수정/삭제, 관리자페이지) 에서
 * 로그인을 했는지 안했는지 검사하여 , 
 * 로그인 되어 있지 않으면 로그인 페이지로 이동하도록 처리
 * 로그인 되어 있으면 그대로 수행 되도록 처리
 */
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

   private static Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);
   
   /**
    * @author Administrator
    * @data 2025. 2. 20.
    * @enclosing_method preHandle
    * @todo TODO
    * @param 
    * @returnType boolean : 참이면 원래의 uri로 계속진행, 거짓이면 interceptor가 끝난것으로 간주
    */
   @Override
   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
         throws Exception {
      
      boolean goOriginPath = false;
      
      if (request.getMethod().toLowerCase().equals("get")) {  // GET방식 요청일 때만 동작 하도록..
         logger.info("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★ preHandle() ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★ ");
         logger.info("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★ 로그인 했는지 안했는지 검사하자 ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★ ");
         HttpSession session = request.getSession(); // 세션객체 얻어오기
         Member loginMember = (Member) session.getAttribute("loginMember");
         if (loginMember == null) {
            System.out.println("로그인 안했지롱~~~~~~~");

            response.sendRedirect("/member/showLoginForm"); // 로그인 페이지로 강제 이동

         } else {
            System.out.println("로그인 되어 있지롱~~~~ : " + loginMember);
            System.out.println(request.getRequestURI());
            if (request.getRequestURI().contains("admin")) { // 요청 주소가 관리자페이지이면..
               if (loginMember.getIsAdmin().equals("Y")) {
                  System.out.println("관리자 양반");
                  goOriginPath = true;
                  return true;
               } else {
                  response.sendRedirect("/member/showLoginForm");
                  return false;
               }
            } else {
               // 로그인 되어 있고, 관리자 페이지를 요청하지 않았다면...
               goOriginPath = true;
            }
         
         } 
      } else {
         // 요청 방식이 GET이 아닐
         goOriginPath = true;
      }
      
      return goOriginPath;
   }

   @Override
   public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
         ModelAndView modelAndView) throws Exception {
      logger.info("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★ postHandle() ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★ ");
      super.postHandle(request, response, handler, modelAndView);
   }
   
}