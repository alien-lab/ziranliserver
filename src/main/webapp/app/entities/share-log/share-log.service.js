(function() {
    'use strict';
    angular
        .module('ziranliserverApp')
        .factory('ShareLog', ShareLog);

    ShareLog.$inject = ['$resource', 'DateUtils'];

    function ShareLog ($resource, DateUtils) {
        var resourceUrl =  'api/share-logs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.shareTime = DateUtils.convertDateTimeFromServer(data.shareTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
