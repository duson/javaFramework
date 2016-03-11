'use strict';

define(['app', 'colorPicker'], function(app) {
	app.directive('colorPickerDir', function() {
	    return {
	        restrict: 'A',
	        scope: {
	        	'color': '='
	        },
	        link: function(scope, element, attrs) {
		    	$(element).colorPicker({showHexField: false});

		    	scope.$watch('color', function(value) {          
			    	element.change();
			    });
			}
	    };
	});
});