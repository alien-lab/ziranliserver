(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('CourseOrderDialogController', CourseOrderDialogController);

    CourseOrderDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CourseOrder', 'WechatUser', 'Course'];

    function CourseOrderDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CourseOrder, WechatUser, Course) {
        var vm = this;

        vm.courseOrder = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.wechatusers = WechatUser.query();
        vm.courses = Course.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.courseOrder.id !== null) {
                CourseOrder.update(vm.courseOrder, onSaveSuccess, onSaveError);
            } else {
                CourseOrder.save(vm.courseOrder, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ziranliserverApp:courseOrderUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.payTime = false;
        vm.datePickerOpenStatus.orderTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
