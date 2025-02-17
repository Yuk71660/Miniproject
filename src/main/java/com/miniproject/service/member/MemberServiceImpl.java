package com.miniproject.service.member;

import org.springframework.stereotype.Service;

import com.miniproject.dao.member.MemberDAO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
   
   private final MemberDAO dao;

   @Override
   public boolean isUserIdDuplicate(String userId) throws Exception {
      boolean result = false;
      
      if (dao.isDuplicate(userId) != null)  {
         result = true;
      }
      
      return result;
   }

}
