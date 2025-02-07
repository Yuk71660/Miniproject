package com.miniproject.dao.pointlog;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.miniproject.model.PointLogDTO;

@Repository
public class PointLogDAOImpl implements PointLogDAO {
	
	@Autowired
	private SqlSession ses;
	
	private static final String NS = "com.miniproject.mapper.pointlogmapper";

	@Override
	public int insertPointLog(PointLogDTO pointLogDTO) throws Exception {
		
		return ses.insert(NS + ".insertPointLog", pointLogDTO);
	}

}