package com.miniproject.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
   
   private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
   
   
   @RequestMapping(value = "/", method = RequestMethod.GET)
   public String home() {
      
      return "index";
   }
   
   @GetMapping(value="/interceptorA")
   public void interceptorA() {
      
      logger.info("interceptorA 메서드 호출됨!!!!!!");
      
      // return "interceptorA";
   }
   
   @GetMapping("/blog")
   public void showBlog() {
      
   }
   
   @PostMapping(value="/blog", produces = "application/json; charset=utf-8")
   public ResponseEntity<String> searchBlog(@RequestParam("keyWord") String keyWord) {
      logger.info(keyWord + "를 검색");
      
      String clientId = "tsADa4ElWGshHO7f0Sxy"; //애플리케이션 클라이언트 아이디
        String clientSecret = "kWh9do7GVp"; //애플리케이션 클라이언트 시크릿
        
        String text = null;
        try {
            text = URLEncoder.encode(keyWord, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패", e);
        }
        
        String apiURL = "https://openapi.naver.com/v1/search/blog?query=" + text;    // JSON 결과
      
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        String responseBody = get(apiURL,requestHeaders);
        
        System.out.println(responseBody);
        
        return new ResponseEntity<String>(responseBody, HttpStatus.OK);
   }
   
    private static String get(String apiUrl, Map<String, String> requestHeaders){
           HttpURLConnection con = connect(apiUrl);
           try {
               con.setRequestMethod("GET");
               for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                   con.setRequestProperty(header.getKey(), header.getValue());
               }


               int responseCode = con.getResponseCode();
               if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                   return readBody(con.getInputStream());
               } else { // 오류 발생
                   return readBody(con.getErrorStream());
               }
           } catch (IOException e) {
               throw new RuntimeException("API 요청과 응답 실패", e);
           } finally {
               con.disconnect();
           }
       }


       private static HttpURLConnection connect(String apiUrl){
           try {
               URL url = new URL(apiUrl);
               return (HttpURLConnection)url.openConnection();
           } catch (MalformedURLException e) {
               throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
           } catch (IOException e) {
               throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
           }
       }
       
       private static String readBody(InputStream body){
           InputStreamReader streamReader = new InputStreamReader(body);


           try (BufferedReader lineReader = new BufferedReader(streamReader)) {
               StringBuilder responseBody = new StringBuilder();


               String line;
               while ((line = lineReader.readLine()) != null) {
                   responseBody.append(line);
               }


               return responseBody.toString();
           } catch (IOException e) {
               throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
           }
       }
   
}
