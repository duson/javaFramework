/**
 * 
 */

require.config({
    baseUrl: "static/apps/",    
    paths: {
    	'jquery': 'lib/jquery/jquery-1.11.3.min',
    	'bootstrap': 'lib/bootstrap/js/bootstrap',
        'angular': 'lib/angular/angular',
        'angular-route': 'lib/angular/angular-route',
        'angular-ui-route': 'lib/angular-ui-router/release/angular-ui-router.min',
        'angular-cookies': 'lib/angular/angular-cookies.min',
        'angularAMD': 'lib/angularAMD/angularAMD',
        'angularStrap': 'lib/angular-strap/angular-strap.min',
		'angularStrap_tpl': 'lib/angular-strap/angular-strap.tpl.min',
		'bootstrapPaginator': 'lib/bootstrap-paginator/bootstrap-paginator.min'
    },
    shim: {
    	'bootstrap': ['jquery'],
    	'angularAMD': ['angular'], 
    	'angular-route': ['angular'],
    	'angular-ui-route': ['angular'],
    	'angular-cookies': ['angular'],
    	'angularStrap': ['angular'],
		'angularStrap_tpl': ['angular', 'angularStrap'],
		'bootstrapPaginator': ['bootstrap']
    },
	map: {
		'*' : {
			'css': 'lib/requirejs/plugins/css'
		}
	},
	urlArgs: 'ver=1.0',
	waitSeconds : 0,
	deps: ['css!lib/bootstrap/css/bootstrap.min.css','css!lib/bootstrap/css/font-awesome.min.css', 'bootstrap']
});
