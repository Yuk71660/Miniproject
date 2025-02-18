package com.miniproject.service.member;

import java.util.List;

import org.springframework.stereotype.Service;

import com.miniproject.dao.member.MemberDAO;
import com.miniproject.model.MemberDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final MemberDAO dao;

	@Override
	public boolean saveNewMember(MemberDTO newMember, List<String> hobbys) throws Exception {
		boolean result = false;
		// toString하면 자동으로 ,붙으니까 배열(리스트)의[]만 substring으로 제거해서 넣기
		if (hobbys == null || hobbys.isEmpty()) {
			System.out.println("취미없을떄?");
		} else {
			String tmp = hobbys.toString();
			newMember.setHobbys(tmp.substring(1, tmp.length() - 1));
		}
		
		if (dao.saveUser(newMember) == 1) {
			result = true;
		}

		return result;
	}

	@Override
	public boolean isUserIdDuplicate(String userId) throws Exception {
		boolean result = false;

		if (dao.isDuplicate(userId) != null) {
			result = true;
		}

		return result;
	}

}
