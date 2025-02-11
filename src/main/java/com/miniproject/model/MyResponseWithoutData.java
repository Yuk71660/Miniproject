package com.miniproject.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
@ToString
public class MyResponseWithoutData {
   private int code;
   private String message;
   
   public static MyResponseWithoutData successResponse() {
      return new MyResponseWithoutData(200, "success");   
   }
   
   public static MyResponseWithoutData failureResponse() {
      return new MyResponseWithoutData(500, "fail");
   }
   
   
}
