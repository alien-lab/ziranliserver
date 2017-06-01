(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('CourseDetailController', CourseDetailController);

    CourseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Course', 'CourseType'];

    function CourseDetailController($scope, $rootScope, $stateParams, previousState, entity, Course, CourseType) {
        var vm = this;

        vm.course = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ziranliserverApp:courseUpdate', function(event, result) {
            vm.course = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
