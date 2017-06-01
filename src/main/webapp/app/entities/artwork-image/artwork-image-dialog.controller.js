(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('ArtworkImageDialogController', ArtworkImageDialogController);

    ArtworkImageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ArtworkImage', 'Artwork'];

    function ArtworkImageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ArtworkImage, Artwork) {
        var vm = this;

        vm.artworkImage = entity;
        vm.clear = clear;
        vm.save = save;
        vm.artworks = Artwork.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.artworkImage.id !== null) {
                ArtworkImage.update(vm.artworkImage, onSaveSuccess, onSaveError);
            } else {
                ArtworkImage.save(vm.artworkImage, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ziranliserverApp:artworkImageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
