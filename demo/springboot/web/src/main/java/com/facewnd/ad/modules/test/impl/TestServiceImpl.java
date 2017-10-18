package com.facewnd.ad.modules.test.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.facewnd.ad.modules.test.TestService;
import com.facewnd.ad.modules.test.dao.UsersMapper;
import com.facewnd.ad.modules.test.entity.Users;
import com.facewnd.core.mybatismapper.MyBatisUtils;

@Service
public class TestServiceImpl implements TestService {
	@Autowired
	private UsersMapper usersMapper;

	private Logger logger = LogManager.getLogger(getClass());
	
	@Override
	public String test(String name) {
		logger.debug("impl - name");
		//if(name.equals("abc"))
		//	throw new RuntimeException("ex");
		return "hello " + name;
	}

	@Override
	public void test1() {
		Users record = new Users();
		record.setId((int)MyBatisUtils.createPrimaryKey(usersMapper));
		record.setNAME("666");
		//usersMapper.insertSelective(record);
	}

}
