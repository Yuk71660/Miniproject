package com.miniproject.dao.blog;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.miniproject.model.BlogPostDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BlogDAOImpl implements BlogDAO {

	private final SqlSession ses; // SqlSession 객체를 생성자를 통해 주입하도록 함

	private static final String NS = "com.miniproject.mapper.blogPostMapper";
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void savePosts(List<BlogPostDto> posts) throws Exception {
		ses.insert(NS + ".insertPosts", posts);
    }

}
