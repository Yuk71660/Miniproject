package com.miniproject.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class BoardUpFilesVODTO {
	private int boardUpfileNo;
	private String originalFileName;
	private String newFileName;
	private String thumbFileName;
	private String fileType;
	private String ext;
	private long size;
	private String base64Image;
	private int boardNo;

}
