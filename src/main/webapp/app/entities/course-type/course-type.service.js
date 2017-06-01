(function() {
    'use strict';
    angular
        .module('ziranliserverApp')
        .factory('CourseType', CourseType);

    CourseType.$inject = ['$resource'];

    function CourseType ($resource) {
        var resourceUrl =  'api/course-types/:id';

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
