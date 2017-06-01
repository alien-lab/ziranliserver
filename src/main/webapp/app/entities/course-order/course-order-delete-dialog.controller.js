(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('CourseOrderDeleteController',CourseOrderDeleteController);

    CourseOrderDeleteController.$inject = ['$uibModalInstance', 'entity', 'CourseOrder'];

    function CourseOrderDeleteController($uibModalInstance, entity, CourseOrder) {
        var vm = this;

        vm.courseOrder = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CourseOrder.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
