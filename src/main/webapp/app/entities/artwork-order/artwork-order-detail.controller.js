(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('ArtworkOrderDetailController', ArtworkOrderDetailController);

    ArtworkOrderDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ArtworkOrder', 'Artwork', 'WechatUser'];

    function ArtworkOrderDetailController($scope, $rootScope, $stateParams, previousState, entity, ArtworkOrder, Artwork, WechatUser) {
        var vm = this;

        vm.artworkOrder = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ziranliserverApp:artworkOrderUpdate', function(event, result) {
            vm.artworkOrder = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
