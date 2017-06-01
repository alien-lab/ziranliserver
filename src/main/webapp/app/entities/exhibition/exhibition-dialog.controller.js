(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('ExhibitionDialogController', ExhibitionDialogController);

    ExhibitionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Exhibition'];

    function ExhibitionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Exhibition) {
        var vm = this;

        vm.exhibition = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.exhibition.id !== null) {
                Exhibition.update(vm.exhibition, onSaveSuccess, onSaveError);
            } else {
                Exhibition.save(vm.exhibition, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ziranliserverApp:exhibitionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
