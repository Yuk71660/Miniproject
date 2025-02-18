package com.miniproject.service.member;

import java.util.List;

import com.miniproject.model.MemberDTO;

public interface MemberService {
	boolean isUserIdDuplicate(String userId) throws Exception;

	boolean saveNewMember(MemberDTO newMember, List<String> hobbys) throws Exception;
}
