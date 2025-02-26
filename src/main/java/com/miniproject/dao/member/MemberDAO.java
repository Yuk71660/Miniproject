package com.miniproject.dao.member;

import java.sql.SQLException;

import com.miniproject.model.LoginDTO;
import com.miniproject.model.Member;
import com.miniproject.model.MemberDTO;

public interface MemberDAO {
	// user에게 지급된 포인트 만큼 userpoint 증감하는 메서드
	int updateUserPoint(String userId, String pointwhy) throws Exception;

	// userId가 중복되는지 검사
	String isDuplicate(String userId) throws Exception;

	int insertMember(MemberDTO newMember) throws Exception;

	Member selectMemberByLoginDTO(LoginDTO loginDTO) throws Exception;

	int saveSessionId(String userId, String sesId) throws SQLException;
}
