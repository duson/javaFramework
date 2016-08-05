/**
 * 控件功能操作权限
 */

define(['app', 'permissionService'], function (app) {
	app.directive('hasPermission', function(PermissionService) {
		
		return {
			link : function(scope, element, attrs) {
				var func = attrs.func; // 功能
				var opera = attrs.opera; // 操作
				if(opera)
					opera = parseInt(opera);

				// 触发控件显隐
				function permissionsChanged() {
					if(!element) return;
					
					var hasPermission = PermissionService.hasPermission(func, opera);

					if (hasPermission)
						element.show();
					else
						element.hide();
				}
				
				permissionsChanged();
				scope.$on('permissionsChanged', permissionsChanged);
			}
		};		
		
	});
});
