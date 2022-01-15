package com.kh.spring.board.model.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BoardEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int attachCount; // 게시물별 첨부파일수
	private List<Attachment> attachments;
	
	public Board(int no, String title, String memberId, String content, Date regDate, int readCount, int attachCount,
			List<Attachment> attachments) {
		super(no, title, memberId, content, regDate, readCount);
		this.attachCount = attachCount;
		this.attachments = attachments;
	}
	
	

	
	
}
