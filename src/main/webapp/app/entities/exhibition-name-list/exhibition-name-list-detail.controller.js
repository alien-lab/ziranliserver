(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('ExhibitionNameListDetailController', ExhibitionNameListDetailController);

    ExhibitionNameListDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ExhibitionNameList', 'WechatUser', 'Exhibition'];

    function ExhibitionNameListDetailController($scope, $rootScope, $stateParams, previousState, entity, ExhibitionNameList, WechatUser, Exhibition) {
        var vm = this;

        vm.exhibitionNameList = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ziranliserverApp:exhibitionNameListUpdate', function(event, result) {
            vm.exhibitionNameList = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
