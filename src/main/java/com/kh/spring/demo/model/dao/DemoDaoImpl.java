package com.kh.spring.demo.model.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.spring.demo.model.vo.Dev;

@Repository
public class DemoDaoImpl implements DemoDao {

	@Autowired // bean을 주세요. application-config.xml에서 의존주입
	private SqlSessionTemplate session;

	@Override
	public int insertDev(Dev dev) {
		return session.insert("demo.insertDev", dev);
	}

	@Override
	public List<Dev> selectAllDev() {
		return session.selectList("demo.selectAllDev");
	}
}
