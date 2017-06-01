(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('ExhibitionNameListDeleteController',ExhibitionNameListDeleteController);

    ExhibitionNameListDeleteController.$inject = ['$uibModalInstance', 'entity', 'ExhibitionNameList'];

    function ExhibitionNameListDeleteController($uibModalInstance, entity, ExhibitionNameList) {
        var vm = this;

        vm.exhibitionNameList = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ExhibitionNameList.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
