(function() {
    'use strict';
    angular
        .module('ziranliserverApp')
        .factory('Course', Course);

    Course.$inject = ['$resource', 'DateUtils'];

    function Course ($resource, DateUtils) {
        var resourceUrl =  'api/courses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.courseTime = DateUtils.convertDateTimeFromServer(data.courseTime);
                        data.createTime = DateUtils.convertDateTimeFromServer(data.createTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },
            'loadImages':{method:'GET',url:'api/course/images/:id',isArray: true}
        });
    }
})();
