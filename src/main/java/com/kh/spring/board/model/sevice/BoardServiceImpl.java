package com.kh.spring.board.model.sevice;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.spring.board.model.dao.BoardDao;
import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class) // 서비스에서만 작성 가능. 다른 곳 xxxxxxXXX
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardDao boardDao;

	@Override
	public List<Board> selectAllBoard(Map<String, Object> param) {
		return boardDao.selectAllBoard(param);
	}

	@Override
	public int selectTotalContent() {
		return boardDao.selectTotalContent();
	}

//	@Transactional(
//			propagation = Propagation.REQUIRED,
//			isolation = Isolation.READ_COMMITTED,
//			rollbackFor = Exception.class)
	@Override
	public int insertBoard(Board board) {
		int result = boardDao.insertBoard(board);
		
		// mapper에서 게시글의 no을 가지고 돌아왔기 때문에 no사용 가능
		List<Attachment> attachments = board.getAttachments();
		if(attachments != null) {
			for(Attachment attach : attachments) {
				// fk컬럼 boardNo값 설정
				attach.setBoardNo(board.getNo());
				result = insertAttachment(attach);
			}
		}
		
		return result;
	}

//	@Transactional(rollbackFor = Exception.class)
	@Override
	public int insertAttachment(Attachment attach) {
		return boardDao.insertAttachment(attach);
	}

	@Override
	public Board selectOneBoard(int no) {
		// 1.board테이블 조회
		Board board = boardDao.selectOneBoard(no);
		
		// 2.attachment테이블 조회
		List<Attachment> attachments = boardDao.selectAttachmentListByBoardNo(no);
		
		// 3.합치기
		board.setAttachments(attachments);
		
		return board;
	}

	@Override
	public Board selectOneBoardCollection(int no) {
		return boardDao.selectOneBoardCollection(no);
	}

	@Override
	public Attachment selectOneAttachment(int no) {
		return boardDao.selectOneAttachment(no);
	}
	
	
}












