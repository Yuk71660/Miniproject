package com.miniproject.service.member;

import com.miniproject.model.LoginDTO;
import com.miniproject.model.Member;
import com.miniproject.model.MemberDTO;

public interface MemberService {
   boolean isUserIdDuplicate(String userId) throws Exception;

   boolean registerMember(MemberDTO newMember) throws Exception;

   Member loginMember(LoginDTO loginDTO) throws Exception;
}