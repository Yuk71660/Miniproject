package com.miniproject.model;

import java.sql.Timestamp;

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
public class Member {
	private String userId, userPwd, userName, mobile, email, gender, job, hobbies, postZip, addr, userImg, isAdmin;
	private Timestamp registerDate;
	private int userpoint;
}
