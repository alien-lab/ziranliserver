(function() {
    'use strict';
    angular
        .module('ziranliserverApp')
        .factory('ArtworkImage', ArtworkImage);

    ArtworkImage.$inject = ['$resource'];

    function ArtworkImage ($resource) {
        var resourceUrl =  'api/artwork-images/:id';

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
