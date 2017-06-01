(function() {
    'use strict';
    angular
        .module('ziranliserverApp')
        .factory('WechatUser', WechatUser);

    WechatUser.$inject = ['$resource'];

    function WechatUser ($resource) {
        var resourceUrl =  'api/wechat-users/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
