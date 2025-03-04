package com.miniproject.model;

import java.sql.Timestamp;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString 
public class Reply {
   private int replyNo;
   private String replyContent;
   private String replyer;
   private Timestamp postDate;
   private int boardNo;
}