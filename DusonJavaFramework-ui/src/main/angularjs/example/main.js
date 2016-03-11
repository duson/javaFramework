/**
 * 
 */

require.config({
	paths : {
		'bootstrap-datetimepicker' : 'lib/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min',
		'moment' : 'lib/moment/moment.min'
	},
	shim : {
		'bootstrap-datetimepicker' : [ 'bootstrap', 'moment', 'css!lib/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min' ]
	},
	map : {
		'*' : {
			'css' : 'lib/requirejs/plugins/css'
		}
	},
	urlArgs : 'ver=1.0',
	waitSeconds : 0,
	deps : [ 'common' ],
	callback : function(common) {
		require([ 'css!../css/main.css', 'app' ]);
	}
});
