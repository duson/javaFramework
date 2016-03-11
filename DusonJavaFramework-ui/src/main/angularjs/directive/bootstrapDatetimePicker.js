'use strict';

define(['app', 'bootstrap-datetimepicker'], function(app) {
	app.directive('bootstrapDatetimePicker', function() {
	    return {
	        restrict: 'A',
	        link: function(scope, element, attrs) {
	        	var options = {
	        		format: 'YYYY-MM-DD HH:mm:ss'
	        	};
	        	if(attrs.minDate)
	        		options = angular.extend(options, {
	        			minDate: attrs.minDate
	        		});
		    	$(element).datetimepicker(options);

		    	$(element).on("dp.change", function (e) {
		    		$(element).change();
		            //$(element).data("DateTimePicker").minDate(e.date);
		        });
			}
	    };
	});
});