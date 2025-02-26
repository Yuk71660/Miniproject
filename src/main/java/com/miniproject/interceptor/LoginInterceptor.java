package com.miniproject.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.miniproject.model.Member;

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

      HttpSession session = request.getSession();
      if (request.getMethod().toLowerCase().equals("post")) {
         logger.info(
               "★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★ 로그인 이후 postHandle() ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★ ");

         System.out.println("니가 가야 할 곳 : " + (String) session.getAttribute("destUrl"));

         Map<String, Object> map = modelAndView.getModel();
         Member loginMember = (Member) map.get("loginMember");
         // 로그인 성공 했다면
         if (loginMember != null) {
            // 로그인 성공
            logger.info("로그인 성공 !!!!!!!!!!!!!!!!!!!!!!");
            // 1) 로그인 유저의 정보를 세션에 바인딩
            session.setAttribute("loginMember", loginMember);
            // 2) 세션에 destUrl 이 있다면 그쪽으로 이동, 없다면 "/"로 이동
            if (session.getAttribute("destUrl") != null) {
               System.out.println("날아감~~~~~~~~~~~");
               response.sendRedirect((String) session.getAttribute("destUrl"));
            } else {
               // destUrl이 없다면.. "/"로 이동
               response.sendRedirect("../");
            }

         } else {
            // 로그인 실패 했다면
            // 로그인 페이지로 이동
            logger.info("로그인 실패 !!!!!!!!!!!!!!!!!!!!!!");
            response.sendRedirect("/member/login?status=loginFail");
         }
      
      }

   }

}
