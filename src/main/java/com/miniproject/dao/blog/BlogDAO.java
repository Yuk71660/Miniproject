package com.miniproject.dao.blog;

import java.sql.SQLException;
import java.util.List;

import com.miniproject.model.BlogPostDto;
import com.miniproject.model.LoginDTO;
import com.miniproject.model.Member;
import com.miniproject.model.MemberDTO;

public interface BlogDAO {

	void savePosts(List<BlogPostDto> posts) throws Exception;
	
}
