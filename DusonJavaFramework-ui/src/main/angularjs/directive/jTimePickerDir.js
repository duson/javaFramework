'use strict';

define(['app', 'jquery-durationpicker'], function(app) {
	app.directive('jTimePickerDir', function() {
	    return {
	        restrict: 'A',
	        scope: {
	        	'ctlValue': '='
	        },
	        link: function(scope, element, attrs) {
		    	$(element).jtimepicker({
		    		value: scope.ctlValue
		    	});
			}
	    };
	});
});