(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('ArtworkImageDeleteController',ArtworkImageDeleteController);

    ArtworkImageDeleteController.$inject = ['$uibModalInstance', 'entity', 'ArtworkImage'];

    function ArtworkImageDeleteController($uibModalInstance, entity, ArtworkImage) {
        var vm = this;

        vm.artworkImage = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ArtworkImage.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
