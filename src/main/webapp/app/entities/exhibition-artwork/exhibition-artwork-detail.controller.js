(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('ExhibitionArtworkDetailController', ExhibitionArtworkDetailController);

    ExhibitionArtworkDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ExhibitionArtwork', 'Exhibition', 'Artwork'];

    function ExhibitionArtworkDetailController($scope, $rootScope, $stateParams, previousState, entity, ExhibitionArtwork, Exhibition, Artwork) {
        var vm = this;

        vm.exhibitionArtwork = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ziranliserverApp:exhibitionArtworkUpdate', function(event, result) {
            vm.exhibitionArtwork = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
