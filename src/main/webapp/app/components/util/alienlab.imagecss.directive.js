(function() {
    'use strict';
    angular
        .module('ziranliserverApp')
        .directive('alienSquare', function(){
            return {
                restrict: 'A',
                scope: false,
                link: function(scope, element, attrs){
                    element.height(element.width());
                    element.css("overflow","hidden");
                }
            }
        });
})();
