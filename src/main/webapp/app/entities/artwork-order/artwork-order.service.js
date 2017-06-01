(function() {
    'use strict';
    angular
        .module('ziranliserverApp')
        .factory('ArtworkOrder', ArtworkOrder);

    ArtworkOrder.$inject = ['$resource', 'DateUtils'];

    function ArtworkOrder ($resource, DateUtils) {
        var resourceUrl =  'api/artwork-orders/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.orderTime = DateUtils.convertDateTimeFromServer(data.orderTime);
                        data.payTime = DateUtils.convertDateTimeFromServer(data.payTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
