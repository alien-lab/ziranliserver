/**
 * Created by 鸠小浅 on 2017/6/5.
 */
(function () {
    'use strict';
    angular
        .module('ziranliserverApp')
        .controller('ExhibitionArtworksDialogController', ExhibitionArtworksDialogController);
    ExhibitionArtworksDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Exhibition', 'ExhibitionArtwork', '$state'];
    function ExhibitionArtworksDialogController($timeout, $scope, $stateParams, $uibModalInstance, entity, Exhibition, ExhibitionArtwork, $state) {
        var vm = this;
        vm.exhibition = entity;
        vm.clear = clear;

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        //找到该展览对应所有艺术品
        function exhibitionArtworks() {
            Exhibition.exhibitionArtworks({id: vm.exhibition.id}, function (data) {
                vm.exhibitionArtworks = data;
            });
        }

        exhibitionArtworks();
        //删除展览艺术品
        vm.deleteArtwork = function (exhibitionArtwork) {
            Exhibition.deleteExhibitionArtwork({id: exhibitionArtwork.id}, function (data) {
                console.log(data);
                exhibitionArtworks();
            });
        }
    }
})();
