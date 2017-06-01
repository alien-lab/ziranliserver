(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('ArtworkDeleteController',ArtworkDeleteController);

    ArtworkDeleteController.$inject = ['$uibModalInstance', 'entity', 'Artwork'];

    function ArtworkDeleteController($uibModalInstance, entity, Artwork) {
        var vm = this;

        vm.artwork = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Artwork.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
