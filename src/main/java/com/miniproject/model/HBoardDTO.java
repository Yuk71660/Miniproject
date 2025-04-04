package com.miniproject.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Setter
@ToString
public class HBoardDTO {
   private int boardNo;
   private String title;
   private String content;
   private String writer;
   
   // 답글 저장을 위해
   private int ref;
   private int step;
   private int refOrder;
}
