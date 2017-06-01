(function() {
    'use strict';
    angular
        .module('ziranliserverApp')
        .factory('Exhibition', Exhibition);

    Exhibition.$inject = ['$resource'];

    function Exhibition ($resource) {
        var resourceUrl =  'api/exhibitions/:id';

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
