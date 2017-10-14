(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('CourseDialogController', CourseDialogController);

    CourseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Course', 'CourseType'];

    function CourseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Course, CourseType) {
        var vm = this;
        vm.uploadurl="./api/image/upload";
        vm.course = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.coursetypes = CourseType.query();
        vm.course.status="正常";
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.course.id !== null) {
                Course.update(vm.course, onSaveSuccess, onSaveError);
            } else {
                Course.save(vm.course, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ziranliserverApp:courseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.courseTime = false;
        vm.datePickerOpenStatus.createTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();

(function(){
    'use strict';
    angular
        .module('ziranliserverApp')
        .controller('CoursePicDialogController', CoursePicDialogController);
    CoursePicDialogController.$inject=['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Course', 'CourseImage'];
    function CoursePicDialogController($timeout, $scope, $stateParams, $uibModalInstance, entity, Course, CourseImage){
        var vm = this;
        vm.uploadurl="./api/image/upload";
        vm.course = entity;
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
        }

        function onSaveSuccess (result) {
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.deleteImage=function(image){
            CourseImage.delete({id:image.id},function(result){
                loadImages();
            });
        }

        loadImages();
        console.log("asdfghj")
        //加载图册图片
        function loadImages(){
            Course.loadImages({id: vm.course.id},function(result){
                vm.images=result;
                console.log(vm.images);
            });
        }


        $scope.$watch("lastimageurl",function(newvalue,oldvalue){
            console.log("new Image",newvalue);
            if(newvalue){
                var image={
                    image:newvalue,
                    course:{
                        id:vm.course.id
                    }
                }
                CourseImage.save(image,function(result){
                    loadImages();
                })
            }
        },true);
    }
})();
