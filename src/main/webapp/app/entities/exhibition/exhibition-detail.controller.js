(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('ExhibitionDetailController', ExhibitionDetailController);

    ExhibitionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Exhibition'];

    function ExhibitionDetailController($scope, $rootScope, $stateParams, previousState, entity, Exhibition) {
        var vm = this;

        vm.exhibition = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ziranliserverApp:exhibitionUpdate', function(event, result) {
            vm.exhibition = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
