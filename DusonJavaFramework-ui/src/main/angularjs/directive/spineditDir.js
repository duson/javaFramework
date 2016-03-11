'use strict';

define(['app', 'jquery-spinedit'], function(app) {
	app.directive('spineditDir', function() {
	    return {
	        restrict: 'A',
	        scope: {
	        	'ngModel': '=',
	        	'valueChanged': '&onValueChanged'
	        },
	        link: function(scope, element, attrs) {
		    	$(element).spinedit({
		        	minimum: attrs.minimum ? parseFloat(attrs.minimum) : 0,
		        	maximum: attrs.maximum ? parseFloat(attrs.maximum) : 9999999,
		        	step: attrs.step ? parseFloat(attrs.step) : 1,
		        	value: parseFloat(scope.ngModel)
		        });

		        scope.$watch('ngModel', function(value) {
			    	element.change();
			    	$(element).spinedit('setValue', value);
			    });

			    $(element).on("valueChanged", function (e) {
			    	scope.ngModel = e.value;
			    	if(scope.valueChanged)
			    		scope.valueChanged();
				    //element.focus();
				});
			}
	    };
	});
});