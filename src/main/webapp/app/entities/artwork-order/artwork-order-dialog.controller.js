(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('ArtworkOrderDialogController', ArtworkOrderDialogController);

    ArtworkOrderDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ArtworkOrder', 'Artwork', 'WechatUser'];

    function ArtworkOrderDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ArtworkOrder, Artwork, WechatUser) {
        var vm = this;

        vm.artworkOrder = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.artworks = Artwork.query();
        vm.wechatusers = WechatUser.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.artworkOrder.id !== null) {
                ArtworkOrder.update(vm.artworkOrder, onSaveSuccess, onSaveError);
            } else {
                ArtworkOrder.save(vm.artworkOrder, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ziranliserverApp:artworkOrderUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.orderTime = false;
        vm.datePickerOpenStatus.payTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
