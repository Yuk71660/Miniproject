package com.miniproject.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Setter
@ToString
public class MemberDTO {
	private String userId, userPwd, userName, mobile, email, gender, job, postZip, streetAddr, detailAddr, userImg;
	private String[] hobby;
}
