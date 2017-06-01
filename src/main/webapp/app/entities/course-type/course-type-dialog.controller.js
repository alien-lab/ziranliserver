(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('CourseTypeDialogController', CourseTypeDialogController);

    CourseTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CourseType'];

    function CourseTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CourseType) {
        var vm = this;

        vm.courseType = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.courseType.id !== null) {
                CourseType.update(vm.courseType, onSaveSuccess, onSaveError);
            } else {
                CourseType.save(vm.courseType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ziranliserverApp:courseTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
