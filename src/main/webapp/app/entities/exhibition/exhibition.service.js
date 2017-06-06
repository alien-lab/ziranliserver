(function () {
    'use strict';
    angular
        .module('ziranliserverApp')
        .factory('Exhibition', Exhibition);

    Exhibition.$inject = ['$resource', 'DateUtils'];

    function Exhibition($resource, DateUtils) {
        var resourceUrl = 'api/exhibitions/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startTime = DateUtils.convertDateTimeFromServer(data.startTime);
                        data.endTime = DateUtils.convertDateTimeFromServer(data.endTime);
                    }
                    return data;
                }
            },
            'update': {method: 'PUT'},
            'loadAllArtworks': {method: 'GET', url: 'api/allArtworks', isArray: true},
            'exhibitionArtworks': {method: 'GET', url: 'api/exhibitionArtworks/:id', isArray: true},
            'deleteExhibitionArtwork': {method: 'DELETE', url: 'api/exhibition-artworks/:id'}
        });
    }
})();
