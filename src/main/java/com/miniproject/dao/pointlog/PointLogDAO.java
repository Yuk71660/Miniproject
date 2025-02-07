package com.miniproject.dao.pointlog;

import com.miniproject.model.PointLogDTO;

public interface PointLogDAO {
	// pointLog테이블에 포인트 지급 내역 저장
	int insertPointLog(PointLogDTO pointLogDTO) throws Exception;
}