(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('ArtworkDialogController', ArtworkDialogController);

    ArtworkDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Artwork', 'ArtworkImage'];

    function ArtworkDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Artwork, ArtworkImage) {
        var vm = this;
        vm.uploadurl="./api/image/upload";
        vm.artwork = entity;
        vm.clear = clear;
        vm.save = save;
        vm.artworkimages = ArtworkImage.query();
        vm.artwork.status="在售";
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.artwork.id !== null) {
                Artwork.update(vm.artwork, onSaveSuccess, onSaveError);
            } else {
                Artwork.save(vm.artwork, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ziranliserverApp:artworkUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

(function(){
    'use strict';
    angular
        .module('ziranliserverApp')
        .controller('ArtworkPicDialogController', ArtworkPicDialogController);
    ArtworkPicDialogController.$inject=['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Artwork', 'ArtworkImage'];
    function ArtworkPicDialogController($timeout, $scope, $stateParams, $uibModalInstance, entity, Artwork, ArtworkImage){
        var vm = this;
        vm.uploadurl="./api/image/upload";
        vm.artwork = entity;
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
            ArtworkImage.delete({id:image.id},function(result){
                loadImages();
            });
        }

        loadImages();
        //加载图册图片
        function loadImages(){
            Artwork.loadImages({id: vm.artwork.id},function(result){
                vm.images=result;
            });
        }


        $scope.$watch("lastimageurl",function(newvalue,oldvalue){
           console.log("new Image",newvalue);
           if(newvalue){
               var image={
                   image:newvalue,
                   artwork:{
                       id:vm.artwork.id
                   }
               }
               ArtworkImage.save(image,function(result){
                   loadImages();
               })
           }
        },true);
    }
})();
