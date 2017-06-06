/**
 * Created by 鸠小浅 on 2017/6/6.
 */
(function () {
    'use strict';
    angular
        .module('ziranliserverApp')
        .controller('ChooseArtworksController', ChooseArtworksController);
    ChooseArtworksController.$inject = ['entity', '$scope', '$stateParams', '$uibModalInstance', 'Exhibition', 'ExhibitionArtwork'];
    function ChooseArtworksController(entity, $scope, $stateParams, $uibModalInstance, Exhibition, ExhibitionArtwork) {
        var vm = this;
        vm.clear = clear;
        vm.save = save;
        vm.getSelects = getSelects;
        vm.exhibition = entity;
        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
        }

        function onSaveSuccess() {
            $uibModalInstance.dismiss('cancel');
            vm.isSaving = false;
        }

        function onSaveError() {
            $uibModalInstance.dismiss('cancel');
            vm.isSaving = false;
        }


        //获取所有艺术品，筛选出没有被该展览选出展示的艺术品
        function loadAllArtworks() {
            Exhibition.loadAllArtworks(function (data) {
                vm.allArtworks = data;
                vm.requireArtworks = [];
                Exhibition.exhibitionArtworks({id: vm.exhibition.id}, function (data) {
                    vm.exhibitionArtworks = data;
                    for (var i = 0; i < vm.allArtworks.length; i++) {
                        for (var j = 0; j < vm.exhibitionArtworks.length; j++) {
                            if (vm.exhibitionArtworks[j].artwork.id == vm.allArtworks[i].id) {
                                vm.allArtworks[i].selected = true;
                            }
                        }
                    }
                    for (var k = 0; k < vm.allArtworks.length; k++) {
                        if (vm.allArtworks[k].selected != true) {
                            vm.requireArtworks.push(vm.allArtworks[k]);
                        }
                    }
                });
            });
        }

        loadAllArtworks();
        //根据选择批量添加该展览艺术品
        function getSelects() {
            loadAllArtworks();
            var selects = [];
            for (var i = 0; i < vm.allArtworks.length; i++) {
                if (vm.allArtworks[i].$isselected) {
                    selects.push(vm.allArtworks[i]);
                }
            }
            for (var j = 0; j < selects.length; j++) {
                ExhibitionArtwork.save({
                    "amount": selects[j].amount,
                    "price": selects[j].price,
                    "status": selects[j].status,
                    "exhibition": vm.exhibition,
                    "artwork": selects[j]
                }, function (data) {
                });
            }
            return selects;
        }
    }
})();
