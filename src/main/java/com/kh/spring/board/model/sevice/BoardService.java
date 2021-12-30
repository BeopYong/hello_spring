package com.kh.spring.board.model.sevice;

import java.util.List;
import java.util.Map;

import com.kh.spring.board.model.vo.Board;

public interface BoardService {

	List<Board> selectAllBoard(Map<String, Object> param);

}
