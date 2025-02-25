package com.miniproject.model;

import java.sql.Timestamp;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
//import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Builder
@Getter
@Setter
@ToString
public class BoardDetailInfo {
	
	private int boardNo;
	private String title;
	private String content;
	private String writer;
	private String isDelete;
	private Timestamp postDate;
	private int readCount;
	private int ref;
	private int step;
	private int refOrder;
	
	private List<BoardUpFilesVODTO> fileList;
	
	private String userId, userName, email, userImg;
	
}
