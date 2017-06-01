(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('ShareLogDialogController', ShareLogDialogController);

    ShareLogDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ShareLog', 'WechatUser'];

    function ShareLogDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ShareLog, WechatUser) {
        var vm = this;

        vm.shareLog = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.wechatusers = WechatUser.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.shareLog.id !== null) {
                ShareLog.update(vm.shareLog, onSaveSuccess, onSaveError);
            } else {
                ShareLog.save(vm.shareLog, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ziranliserverApp:shareLogUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.shareTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
