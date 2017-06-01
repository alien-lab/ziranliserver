(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('ExhibitionArtworkDialogController', ExhibitionArtworkDialogController);

    ExhibitionArtworkDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ExhibitionArtwork', 'Exhibition', 'Artwork'];

    function ExhibitionArtworkDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ExhibitionArtwork, Exhibition, Artwork) {
        var vm = this;

        vm.exhibitionArtwork = entity;
        vm.clear = clear;
        vm.save = save;
        vm.exhibitions = Exhibition.query();
        vm.artworks = Artwork.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.exhibitionArtwork.id !== null) {
                ExhibitionArtwork.update(vm.exhibitionArtwork, onSaveSuccess, onSaveError);
            } else {
                ExhibitionArtwork.save(vm.exhibitionArtwork, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ziranliserverApp:exhibitionArtworkUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
