package com.miniproject.service.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.miniproject.dao.member.MemberDAO;
import com.miniproject.dao.pointlog.PointLogDAO;
import com.miniproject.model.LoginDTO;
import com.miniproject.model.Member;
import com.miniproject.model.MemberDTO;
import com.miniproject.model.PointLogDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

   private final MemberDAO dao;
   private final PointLogDAO pdao;

   @Override
   public boolean isUserIdDuplicate(String userId) throws Exception {
      boolean result = false;

      if (dao.isDuplicate(userId) != null) {
         result = true;
      }

      return result;
   }

   @Override
   @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
   public boolean registerMember(MemberDTO newMember) throws Exception {
      boolean result = false;

      // 1) newMember를 테이블에 insert하기 전에 수행해야 할 작업(취미를 하나의 문자열로 만들기, 도로명주소 + 상세 주소를 하나의
      // 문자열로 합치기)을 완료하고, dao 단 호출

      // 테이블에 저장하기 전
      // 취미를 하나의 문자열로 만들기
      String hobbies = "";
      if (newMember.getHobby() != null) {
         String[] hobby = newMember.getHobby();
         for (int i = 0; i < hobby.length; i++) {
            if (i < hobby.length - 1) {
               hobbies += hobby[i] + ", ";
            } else {
               hobbies += hobby[i];
            }
         }
      }

      newMember.setHobbies(hobbies);

      // 도로명주소 + 상세 주소를 하나의 문자열로 합치기
      newMember.setAddr(newMember.getStreetAddr() + " " + newMember.getDetailAddr());

//      System.out.println(newMember.toString());

      if (dao.insertMember(newMember) == 1) {
         // 2) pointLog 테이블에 포인트 내역 insert
         PointLogDTO pdto = PointLogDTO.builder().pointwho(newMember.getUserId()).pointwhy("회원가입").build();

         if (pdao.insertPointLog(pdto) == 1) {
            // 3) member테이블 회원가입한 회원에게 point update
            if (dao.updateUserPoint(newMember.getUserId(), "회원가입") == 1) {
               result = true;
            }
         }

      }

      return result;
   }

   @Override
   @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
   public Member loginMember(LoginDTO loginDTO) throws Exception {
      
      Member loginMember = dao.selectMemberByLoginDTO(loginDTO);
      if (loginMember != null) {
         // 2) pointLog 테이블에 포인트 내역 insert
         PointLogDTO pdto = PointLogDTO.builder().pointwho(loginMember.getUserId()).pointwhy("로그인").build();
         
         if (pdao.insertPointLog(pdto) == 1) {
            // 3) member테이블 로그인한 회원에게 point update
            dao.updateUserPoint(loginMember.getUserId(), "로그인");

         }
      }

      return loginMember;
   }

}
