package com.miniproject.util;

import java.util.HashMap;
import java.util.Map;

public class ImageMimeType {
	private static Map<String, String> imageMimeType;
	
	{
		// 인스턴스 멤버 초기화 초기화 블럭
	}
	
	static {
		imageMimeType = new HashMap<String, String>();
		
		imageMimeType.put("jpg", "image/jpeg");
		imageMimeType.put("jpeg", "image/jpeg");
		imageMimeType.put("gif", "image/gif");
		imageMimeType.put("png", "image/png");
	}
	
	// 확장자를 받아 확장자 문자열이 map에 있는지 없는지.. 있다면 image 
	public static boolean isImage(String ext) {
		return imageMimeType.containsKey(ext);
	}
	
}