package com.facewnd.core.mybatismapper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class MyBatisUtils {
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public static long createPrimaryKey(BaseMapper<?> mapper) {
		Map<String, Object> pkMap = new HashMap<String, Object>();
		mapper.generatedID(pkMap);
		Long newId = (Long) pkMap.get("longID");
		return newId;
	}
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public static long createPrimaryKey(BaseMapper<?> mapper,Integer idCount) {
		Map<String, Object> pkMap = new HashMap<String, Object>();
		pkMap.put("idCount", idCount);
		mapper.generatedIDExt(pkMap);
		Long newId = (Long) pkMap.get("longID");
		return newId;
	}
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public static long createPrimaryKey(BaseMapper<?> mapper,String tableName,Integer idCount) {
		Map<String, Object> pkMap = new HashMap<String, Object>();
		pkMap.put("tableName", tableName);
		pkMap.put("idCount", idCount);
		mapper.generatedIDByTableNameExt(pkMap);
		Long newId = (Long) pkMap.get("longID");
		return newId;
	}
}
