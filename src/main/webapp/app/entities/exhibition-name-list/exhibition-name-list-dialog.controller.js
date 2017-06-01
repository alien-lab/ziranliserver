(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('ExhibitionNameListDialogController', ExhibitionNameListDialogController);

    ExhibitionNameListDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ExhibitionNameList', 'WechatUser', 'Exhibition'];

    function ExhibitionNameListDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ExhibitionNameList, WechatUser, Exhibition) {
        var vm = this;

        vm.exhibitionNameList = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.wechatusers = WechatUser.query();
        vm.exhibitions = Exhibition.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.exhibitionNameList.id !== null) {
                ExhibitionNameList.update(vm.exhibitionNameList, onSaveSuccess, onSaveError);
            } else {
                ExhibitionNameList.save(vm.exhibitionNameList, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ziranliserverApp:exhibitionNameListUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.joinTime = false;
        vm.datePickerOpenStatus.signTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
