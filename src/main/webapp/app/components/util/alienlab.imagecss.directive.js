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
        })
        .directive('alienImage', function(){
            return {
                restrict: 'A',
                scope: {
                    image:"@"
                },
                link: function(scope, element, attrs){
                    console.log($(element));
                    element.css("height",element.width()+"px");
                    element.css("background-image","url('"+scope.image+"')");
                    element.css("background-size","cover");
                    element.css("background-position","center center");
                    element.css("overflow","hidden");
                }
            }
        });
})();
