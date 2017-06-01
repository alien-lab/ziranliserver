(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('CourseOrderDetailController', CourseOrderDetailController);

    CourseOrderDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CourseOrder', 'WechatUser', 'Course'];

    function CourseOrderDetailController($scope, $rootScope, $stateParams, previousState, entity, CourseOrder, WechatUser, Course) {
        var vm = this;

        vm.courseOrder = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ziranliserverApp:courseOrderUpdate', function(event, result) {
            vm.courseOrder = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
