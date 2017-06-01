(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('ShareLogDeleteController',ShareLogDeleteController);

    ShareLogDeleteController.$inject = ['$uibModalInstance', 'entity', 'ShareLog'];

    function ShareLogDeleteController($uibModalInstance, entity, ShareLog) {
        var vm = this;

        vm.shareLog = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ShareLog.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
