(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('ArtworkImageDetailController', ArtworkImageDetailController);

    ArtworkImageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ArtworkImage', 'Artwork'];

    function ArtworkImageDetailController($scope, $rootScope, $stateParams, previousState, entity, ArtworkImage, Artwork) {
        var vm = this;

        vm.artworkImage = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ziranliserverApp:artworkImageUpdate', function(event, result) {
            vm.artworkImage = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
