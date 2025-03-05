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
import com.mysql.cj.util.StringUtils;

public class LoginInterceptor extends HandlerInterceptorAdapter {
   private static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
   
   @Autowired
   private MemberDAO dao;

   @Override
   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
         throws Exception {

      boolean isShowLoginPage = false;
      if (request.getMethod().toLowerCase().equals("get")) {
         HttpSession ses = request.getSession();
         logger.info(
               "★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★ 로그인 이전 preHandle() ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★ ");
         logger.info("요청 페이지 : " + request.getRequestURI());
         //logger.info("요청 쿼리스트링 : " + request.getQueryString());
         // 댓글 저장/수정/삭제 시 로그인 하러 왔다면....
         if (request.getQueryString() != null) {
            if (request.getQueryString().contains("redirectUrl")) {
               String returnUrl = "/rboard/viewBoard?boardNo=" + request.getParameter("boardNo");
               ses.setAttribute("destUrl", returnUrl);
            }
         }
         
         String cookieVal = readCookie(request);
         System.out.println("읽어낸 쿠키 값 : " + cookieVal);
         if (!StringUtils.isNullOrEmpty(cookieVal)) {
            // cookieVal와 DB에 저장된 세션값과 비교
            Member autoLoginMember = dao.selectSessionID(cookieVal);
            ses.setAttribute("loginMember", autoLoginMember); // 자동로그인
            response.sendRedirect(
                  (ses.getAttribute("destUrl") != null) ? (String) ses.getAttribute("destUrl") : "../");

         } else {
            // 자동로그인 하지 않음
            isShowLoginPage = true;
         } 
      } else {
         isShowLoginPage = true;
      }
      
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
    * @enclosing_method readCookie
    * @todo 자동로그인에서 저장한 쿠키의 값을 읽어온다
    * @param 
    * @returnType void
    */
   private String readCookie(HttpServletRequest request) {
      String cookieVal = null;
      for (Cookie c : request.getCookies()) {
         if (c.getName().equals("autoLogin")) {
            cookieVal = c.getValue();
            break;
         }
      }
      
      return cookieVal;
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
