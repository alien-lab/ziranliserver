(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('ArtworkDialogController', ArtworkDialogController);

    ArtworkDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Artwork', 'ArtworkImage'];

    function ArtworkDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Artwork, ArtworkImage) {
        var vm = this;

        vm.artwork = entity;
        vm.clear = clear;
        vm.save = save;
        vm.artworkimages = ArtworkImage.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.artwork.id !== null) {
                Artwork.update(vm.artwork, onSaveSuccess, onSaveError);
            } else {
                Artwork.save(vm.artwork, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ziranliserverApp:artworkUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
