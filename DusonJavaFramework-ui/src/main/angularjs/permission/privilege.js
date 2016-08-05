'use strict';

define(function() {

	var privilege = {
		Test: 'Test', // Test
	};
	
	var operation = {
		// ----------------通用
		View: 0, // 查看
		Add: 1, // 增加
		Delete: 2, // 删除
		Update: 4, // 修改
		
	};
	
	return {
		Privilege: privilege,
		Operation: operation
	};
	
});

