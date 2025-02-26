package com.miniproject.interceptor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.miniproject.dao.member.MemberDAO;
import com.miniproject.model.Member;

public class LoginInterceptor extends HandlerInterceptorAdapter {
   private static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
   
   @Autowired
   private MemberDAO dao;

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
            //   if (session.getAttribute("destUrl") != null) {
//               System.out.println("날아감~~~~~~~~~~~");
//               response.sendRedirect((String) session.getAttribute("destUrl"));
//            } else {
//               // destUrl이 없다면.. "/"로 이동
//               response.sendRedirect("../");
//            }
//            
            
            System.out.println("자동 로그인 체크? " +  request.getParameter("remember")); // 체크 : on, 체크 x : null
            if (request.getParameter("remember") != null) {
               // 자동로그인에 체크 됨
               // 세션 아이디를 얻어와 유저의 컴퓨터에 저장(쿠키를 이용해), db에도 저장
               String sesId = session.getId();
               System.out.println("세션 아이디 : " + sesId);
               saveCookie(response, sesId, loginMember.getUserId());
            } else {
               // 자동로그인에 체크 하지 않음               
            }
            
            response.sendRedirect((session.getAttribute("destUrl") != null)?  (String)session.getAttribute("destUrl") : "../");

         } else {
            // 로그인 실패 했다면
            // 로그인 페이지로 이동
            logger.info("로그인 실패 !!!!!!!!!!!!!!!!!!!!!!");
            response.sendRedirect("/member/login?status=loginFail");
         }
      
      }

   }

   /**
    * @author Administrator
    * @data 2025. 2. 26.
    * @enclosing_method saveCookie
    * @todo 세션 아이디를 얻어와 유저의 컴퓨터에 저장(쿠키를 이용해), db에도 저장
    * @param 
    * @throws IOException 
    * @returnType void
    */
   private void saveCookie(HttpServletResponse response, String sesId, String userId) throws IOException {
      try {
         if (dao.saveSessionId(userId, sesId) == 1) {
            System.out.println("쿠키 저장!!!!!!!!!!!!!");
            Cookie autoLoginCookie = new Cookie("autoLogin", sesId);
            autoLoginCookie.setPath("/");
            autoLoginCookie.setMaxAge(60 * 60 * 24 * 7);   // 7일동안 쿠키가 살아 있도록
            
            response.addCookie(autoLoginCookie); // 쿠키 저장
            
         }
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         response.sendRedirect("/member/login?status=loginFail");
      }
   }

}
