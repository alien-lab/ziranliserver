(function() {
    'use strict';
    angular
        .module('ziranliserverApp')
        .factory('CourseOrder', CourseOrder);

    CourseOrder.$inject = ['$resource', 'DateUtils'];

    function CourseOrder ($resource, DateUtils) {
        var resourceUrl =  'api/course-orders/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.payTime = DateUtils.convertDateTimeFromServer(data.payTime);
                        data.orderTime = DateUtils.convertDateTimeFromServer(data.orderTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
