package com.miniproject.service.board;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.miniproject.dao.board.HBoardDAO;
import com.miniproject.dao.member.MemberDAO;
import com.miniproject.dao.pointlog.PointLogDAO;
import com.miniproject.model.BoardDetailInfo;
import com.miniproject.model.BoardUpFilesVODTO;
import com.miniproject.model.HBoard;
import com.miniproject.model.HBoardDTO;
import com.miniproject.model.PointLogDTO;

import lombok.RequiredArgsConstructor;

//서비스에서 해야 할 일
// 서비스 비즈니스 로직 처리
// 트랜잭션 처리
// DAO단으로 넘겨주기

@Service
@RequiredArgsConstructor
public class HBoardServiceImpl implements HBoardService {

	private static Logger logger = LoggerFactory.getLogger(HBoardServiceImpl.class);
	
	private final HBoardDAO hdao;
	private final PointLogDAO pdao;
	private final MemberDAO mdao;
	
	@Override
	public List<HBoard> getEntireHBoard() throws Exception {
		// TODO Auto-generated method stub
		logger.info("게시글 전체 리스트 가져오기");
		
		List<HBoard> list = hdao.selectAllHBoard();
		for (HBoard h: list) {
			System.out.println(h.toString());
		}
		
		return list;
		
//		return dao.selectAllHBoard();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public boolean saveBoard(HBoardDTO newBoard, List<BoardUpFilesVODTO> fileList) throws Exception {
		boolean result = false;
		// 게시글을 저장하는 트랜잭션
		newBoard.setContent(newBoard.getContent().replace("\n", "<br />").replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;"));
		// 트랜잭션의 기본 원칙 : All commit or Nothing => 전부 성공할 때 all commit, 하나라도 실패하면 rollback
		if (hdao.insertHBoard(newBoard) > 0) { // hBoard테이블에 insert 
			if (fileList.size() > 0) {
				int newBoardNo = newBoard.getBoardNo();
				for (BoardUpFilesVODTO file : fileList) {
					file.setBoardNo(newBoardNo);
					hdao.insertHBoardUpfile(file);
				}
			}
			
			// 글 작성자에게 포인트 지급 (pointlog테이블 insert)
			PointLogDTO dto = PointLogDTO.builder()
					.pointwho(newBoard.getWriter())
					.pointwhy("게시글작성")
					.build();
							
			if (pdao.insertPointLog(dto) == 1) {
				// 글 작성자 포인트 업데이트(member테이블에 userpoint update)
				if (mdao.updateUserPoint(newBoard.getWriter(), "게시글작성") == 1) {
					result = true;
				}
			}
		}
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
	public BoardDetailInfo getBoardDetailInfo(int boardNo, String ipAddr) throws Exception {
		
		int timediff = hdao.checkHourReadLogByBoardNo(ipAddr, boardNo);
		logger.info("조회 시간 차 : " + timediff);
		if (timediff == -1) {  // 이전 조회기록이 없거나, 24시간 이상 지났을 경우
			// 조회수 증가
			// 조회 기록 insert
			
		} 
		
		BoardDetailInfo bi = hdao.selectBoardDetailInfo(boardNo);
		System.out.println(bi.toString());
		
		return bi;
	}
	
}
