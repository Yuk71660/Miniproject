package com.miniproject.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SampleInterceptor extends HandlerInterceptorAdapter {
   private static Logger logger = LoggerFactory.getLogger(SampleInterceptor.class);

   // 해당 uri에 매핑된 controller단의 메서드 호출 전에 preHandle() 수행
   @Override
   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
         throws Exception {
      
      logger.info("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★ preHandle() ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★ ");
      
      return super.preHandle(request, response, handler);
   }

   // 해당 uri에 매핑된 controller단의 메서드 끝난 직후에 postHandle() 수행
   @Override
   public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
         ModelAndView modelAndView) throws Exception {
      
      logger.info("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★ postHandle() ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★ ");
      
      super.postHandle(request, response, handler, modelAndView);
   }

   // view단까지 응답 후에 실행
   @Override
   public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
         throws Exception {
      logger.info("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★ afterCompletion() ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★ ");
      
      super.afterCompletion(request, response, handler, ex);
   }
   
}
