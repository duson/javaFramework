package com.facewnd.core.mybatismapper;

import java.util.HashMap;
import java.util.Map;

public class MyBatisUtils {
	public static long createPrimaryKey(BaseMapper mapper) {
		Map<String, Object> pkMap = new HashMap<String, Object>();
		mapper.generatedID(pkMap);
		Long newId = (Long) pkMap.get("longID");
		return newId;
	}

}
