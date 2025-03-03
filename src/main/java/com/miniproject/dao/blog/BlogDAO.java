package com.miniproject.dao.blog;

import java.util.List;

import com.miniproject.model.BlogPostDto;

public interface BlogDAO {

	void savePosts(List<BlogPostDto> posts) throws Exception;
	
}
