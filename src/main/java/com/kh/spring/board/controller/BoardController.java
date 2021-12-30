package com.kh.spring.board.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.spring.board.model.sevice.BoardService;
import com.kh.spring.board.model.vo.Board;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/board")
@Slf4j
public class BoardController {
	
	@Autowired
	private BoardService boardService;

	@GetMapping("/boardList.do")
	// @RequestParam(required=true) --> 기본값. 값이 없을 수도 있으니(?) defaultValue="1"로 지정
	public void boardList(@RequestParam(defaultValue = "1") int cPage, Model model) {
		
		// 한 페이지에서 보여줄 컨텐츠의 개수
		int limit = 10;
		int offset = (cPage - 1) * limit;
		Map<String, Object> param = new HashMap<>();
		param.put("offset", offset);
		param.put("limit", limit);
		
		List<Board> boardList = boardService.selectAllBoard(param);
		log.debug("boardList = {}", boardList);
		model.addAttribute("boardList", boardList);
	}
}

















