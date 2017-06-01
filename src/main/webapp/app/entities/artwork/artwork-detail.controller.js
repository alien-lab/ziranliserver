(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('ArtworkDetailController', ArtworkDetailController);

    ArtworkDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Artwork', 'ArtworkImage'];

    function ArtworkDetailController($scope, $rootScope, $stateParams, previousState, entity, Artwork, ArtworkImage) {
        var vm = this;

        vm.artwork = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ziranliserverApp:artworkUpdate', function(event, result) {
            vm.artwork = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
