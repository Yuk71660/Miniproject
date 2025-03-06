package com.miniproject.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component  // 아래의 객체를 스프링 빈으로 만들어 등록
@Aspect // 아래의 객체를 AOP 객체로 사용할 것임.
public class SampleAdvice {
   private static Logger logger = LoggerFactory.getLogger(SampleAdvice.class);
   
   @Before("execution(public * com.miniproject.service.board.*ServiceImpl.saveBoard(..))")
   public void startAOP() {
      logger.info("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★ startAOP ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★ ");
   }
   
   @After("execution(public * com.miniproject.service.board.*ServiceImpl.saveBoard(..))")
   public void endAOP() {
      logger.info("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★ endAOP ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★ ");
   }
   
}
