package com.miniproject.dao.member;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.miniproject.model.LoginDTO;
import com.miniproject.model.Member;
import com.miniproject.model.MemberDTO;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberDAOImpl implements MemberDAO {

   private final SqlSession ses;  // SqlSession 객체를 생성자를 통해 주입하도록 함
   
   private static final String NS = "com.miniproject.mapper.membermapper";

//   @RequiredArgsConstructor 에 의해 생성되는 생성자
//   public MemberDAOImpl(SqlSession ses) {
//      this.ses = ses;
//   }
   
   @Override
   public int updateUserPoint(String userId, String pointwhy) throws Exception {
      
      // SqlSession의 insert, update, delete 메서드는 parameter를 한개 밖에 넣을 수 없다.
      // DTO객체가 있다면 그 DTO 객체를 넣으면 되지만, 
      // 지금의 경우에는 userId와 pointscore를 가지고 있는 DTO가 없기 때문에 매개변수 2개를 넣지 못하는 현상이 발생한다
      // => Map객체를 활용하여 매개변수를 세팅한다
      
      Map<String, Object> params = new HashMap<String, Object>();
      params.put("pointwhy", pointwhy);
      params.put("userId", userId);
      
      return ses.update(NS + ".updateUserPoint", params);
      
   }

   @Override
   public String isDuplicate(String userId) throws Exception {
      
      return ses.selectOne(NS + ".isUserIdDuplicate", userId);
   }

   @Override
   public int insertMember(MemberDTO newMember) throws Exception {
      
      return ses.insert(NS + ".insertNewMember", newMember);
      
   }

   @Override
   public Member selectMemberByLoginDTO(LoginDTO loginDTO) throws Exception {
      
      return ses.selectOne(NS + ".loginMember", loginDTO);
   }

   @Override
   public int saveSessionId(String userId, String sesId) throws SQLException {
      Map<String, Object> params = new HashMap<String, Object>();
      params.put("userId", userId);
      params.put("sesId", sesId);
      
      return ses.update(NS + ".saveSessionID" , params);
   }

}
