(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('CourseTypeDetailController', CourseTypeDetailController);

    CourseTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CourseType'];

    function CourseTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, CourseType) {
        var vm = this;

        vm.courseType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ziranliserverApp:courseTypeUpdate', function(event, result) {
            vm.courseType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
