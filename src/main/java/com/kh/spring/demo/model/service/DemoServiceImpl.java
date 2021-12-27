package com.kh.spring.demo.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.demo.model.dao.DemoDao;
import com.kh.spring.demo.model.vo.Dev;

@Service
public class DemoServiceImpl implements DemoService {

	@Autowired
	private DemoDao demoDao;

	@Override
	public int insertDev(Dev dev) {
		// 트랜잭션 처리해야 하나 일단은 심플하게 가보기
		return demoDao.insertDev(dev);
	}

	@Override
	public List<Dev> selectAllDev() {
		return demoDao.selectAllDev();
	}

	
	
}
