package com.kh.spring.memo.model.dao;

import java.util.List;

import com.kh.spring.memo.model.vo.Memo;

public interface MemoDao {

	List<Memo> selectAllMemo();

	int insertMemo(Memo memo);

	int deleteMemo(int no);

	Memo selectOneMemo(int no);

	
}
