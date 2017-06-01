(function() {
    'use strict';
    angular
        .module('ziranliserverApp')
        .factory('ExhibitionArtwork', ExhibitionArtwork);

    ExhibitionArtwork.$inject = ['$resource'];

    function ExhibitionArtwork ($resource) {
        var resourceUrl =  'api/exhibition-artworks/:id';

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
