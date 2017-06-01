(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('ExhibitionArtworkDeleteController',ExhibitionArtworkDeleteController);

    ExhibitionArtworkDeleteController.$inject = ['$uibModalInstance', 'entity', 'ExhibitionArtwork'];

    function ExhibitionArtworkDeleteController($uibModalInstance, entity, ExhibitionArtwork) {
        var vm = this;

        vm.exhibitionArtwork = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ExhibitionArtwork.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
