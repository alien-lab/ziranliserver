(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('ExhibitionDeleteController',ExhibitionDeleteController);

    ExhibitionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Exhibition'];

    function ExhibitionDeleteController($uibModalInstance, entity, Exhibition) {
        var vm = this;

        vm.exhibition = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Exhibition.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
