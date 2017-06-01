(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .controller('ShareLogDetailController', ShareLogDetailController);

    ShareLogDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ShareLog', 'WechatUser'];

    function ShareLogDetailController($scope, $rootScope, $stateParams, previousState, entity, ShareLog, WechatUser) {
        var vm = this;

        vm.shareLog = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ziranliserverApp:shareLogUpdate', function(event, result) {
            vm.shareLog = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
