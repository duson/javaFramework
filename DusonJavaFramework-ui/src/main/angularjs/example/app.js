'use strict';

define([ 'angularAMD', 'angular-route', 'angular-ui-route', 'angularStrap_tpl'], function(angularAMD) {
	var app = angular.module("module", [ 'ngRoute', 'ui.router', 'mgcrea.ngStrap' ]);

	app.run(function($rootScope, $http) {
		$rootScope.pageTitle = "xx系统";
		$rootScope.isLogin = false; // 默认未登陆，可通过Ajax请求后台Session判断是否登录
		
		// 路由变动触发
		$rootScope.$on("$stateChangeStart", function(event, next, current) {
			
		});

	});

	app.config(function($routeProvider, $httpProvider, $stateProvider, $urlRouterProvider) {
		$httpProvider.interceptors.push('authInterceptor');
		$httpProvider.defaults.withCredentials = true;
		$httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
		$httpProvider.defaults.headers.post['Accept'] = 'application/json, text/javascript, */*; q=0.01';
		$httpProvider.defaults.headers.post['X-Requested-With'] = 'XMLHttpRequest';
		$httpProvider.defaults.transformRequest = [ function(data) {
			return angular.isObject(data) ? $.param(data) : data;
		}];

		$urlRouterProvider.otherwise("/");
		$urlRouterProvider.when('/xxx', '/');
		
		/* 首页 */
		$stateProvider.state("/", angularAMD.route({
			url : "/",
			templateUrl : 'views/index.html',
			controller : 'Ctrl',
			controllerUrl : 'main/controllers/index'
		}));

	});
	app.factory('authInterceptor', [ "$rootScope", function($rootScope) {
		var reqexption = {
			responseError : function(response) {
				if (response.status == 404) {
					alert(response.data);
				} else if (response.status == 911) { // Session过期
					window.location.reload();
				}
				return response;
			}
		};
		return reqexption;
	} ]);
	return angularAMD.bootstrap(app);
});