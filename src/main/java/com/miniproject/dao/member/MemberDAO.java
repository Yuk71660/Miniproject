package com.miniproject.dao.member;

public interface MemberDAO {
	// user에게 지급된 포인트 만큼 userpoint 증감하는 메서드
	int updateUserPoint(String userId, String pointwhy) throws Exception;
	
	String isDuplicate (String userId) throws Exception;
}