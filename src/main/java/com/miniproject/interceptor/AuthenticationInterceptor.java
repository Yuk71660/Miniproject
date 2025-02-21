package com.miniproject.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.miniproject.dao.board.HBoardDAO;
import com.miniproject.model.Member;
import com.miniproject.service.board.HBoardService;

import lombok.RequiredArgsConstructor;

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
   
   @Autowired
   private HBoardDAO dao;
   
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

         } else {  // 로그인 되었다면
            System.out.println("로그인 되어 있지롱~~~~ : " + loginMember);
            System.out.println("요청 주소 : " + request.getRequestURI());
            if (request.getRequestURI().contains("admin")) { // 요청 주소가 관리자페이지이면..
               if (loginMember.getIsAdmin().equals("Y")) {
                  System.out.println("관리자 양반");
                  return true;
               } else {
                  response.sendRedirect("/member/showLoginForm");
                  return false;
               }
            } else if(request.getRequestURI().contains("modify") || request.getRequestURI().contains("remove")) {
               // 로그인 되어 있고,  글 수정/글삭제 페이지를 요청 했다면... 로그인한유저가 작성한 글인지 확인해봐야 한다..
//               System.out.println("요청된 쿼리스트링 : " + request.getQueryString());
               int boardNo = Integer.parseInt(request.getParameter("boardNo"));
//               System.out.println(dao.toString() + "," + boardNo);
               
               String writer = dao.selectWriterByBoardNo(boardNo);
               if (loginMember.getUserId().equals(writer)) {
                  goOriginPath = true;
               } else {
                  response.sendRedirect("/hboard/viewBoard?boardNo=" + boardNo + "&status=notallowed");
                  goOriginPath = false;
               }
               
            } else {
               // 로그인 되어 있고, 관리자 페이지, 글 수정/글삭제 페이지를 요청하지 않았다면...
               goOriginPath = true;
            }
         
         } 
      } else {
         // 요청 방식이 GET이 아닐
         goOriginPath = true;
      }
      
      return goOriginPath;
   }

}
