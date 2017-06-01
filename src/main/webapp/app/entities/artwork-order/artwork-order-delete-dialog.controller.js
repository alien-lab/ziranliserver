(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('ArtworkOrderDeleteController',ArtworkOrderDeleteController);

    ArtworkOrderDeleteController.$inject = ['$uibModalInstance', 'entity', 'ArtworkOrder'];

    function ArtworkOrderDeleteController($uibModalInstance, entity, ArtworkOrder) {
        var vm = this;

        vm.artworkOrder = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ArtworkOrder.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
