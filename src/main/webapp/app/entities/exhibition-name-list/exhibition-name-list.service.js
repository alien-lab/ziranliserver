(function() {
    'use strict';
    angular
        .module('ziranliserverApp')
        .factory('ExhibitionNameList', ExhibitionNameList);

    ExhibitionNameList.$inject = ['$resource', 'DateUtils'];

    function ExhibitionNameList ($resource, DateUtils) {
        var resourceUrl =  'api/exhibition-name-lists/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.joinTime = DateUtils.convertDateTimeFromServer(data.joinTime);
                        data.signTime = DateUtils.convertDateTimeFromServer(data.signTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
