(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('CourseTypeDeleteController',CourseTypeDeleteController);

    CourseTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'CourseType'];

    function CourseTypeDeleteController($uibModalInstance, entity, CourseType) {
        var vm = this;

        vm.courseType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CourseType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
