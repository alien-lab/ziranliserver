(function() {
    'use strict';
    angular
        .module('ziranliserverApp')
        .factory('Artwork', Artwork);

    Artwork.$inject = ['$resource'];

    function Artwork ($resource) {
        var resourceUrl =  'api/artworks/:id';

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
