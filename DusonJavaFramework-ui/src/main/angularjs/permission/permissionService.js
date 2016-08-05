/**
 * 功能操作权限服务
 */

define(['angular', 'jquery'], function(a, $) {
	var app = window.angular.module('commonModule', []);

	app.factory('PermissionService', function ($rootScope) {
	    	
		var permissionList;
		
		function getPermission() {
			$.ajax({
				url: "getPermission?r=" + Math.random(),
				type: 'Post',
				dataType: 'json',
				async: false,
				success: function(data) {
					permissionList = data.result;
				},
				error: function(error) {
					alert('获取权限集失败');
				}
			});
		}
		
	    return {
	      setPermissions: function(permissions) { // 设置权限
	        permissionList = permissions;
	        $rootScope.$broadcast('permissionsChanged')
	      },
	      hasPermission: function (func, opera) { // 判断权限
	    	  if(!permissionList) getPermission();
	    	  if(!permissionList) return true;
	    	  
	    	  var hasPermission = false;
	        
	    	  if(!opera) opera = 0;
	    	  angular.forEach(permissionList, function(item) {
	    		  if(item.strVal1 === func && (opera === 0 || item.operationMask & opera)) {
	    			  hasPermission = true;
	    			  return;
	    		  }
	    	  });
	        
	    	  return hasPermission;
	      }
	    };
	      
	});
	
});
