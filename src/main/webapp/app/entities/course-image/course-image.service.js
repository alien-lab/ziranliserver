/**
 * Created by 鸠小浅 on 2017/10/14.
 */
(function() {
    'use strict';
    angular
        .module('ziranliserverApp')
        .factory('CourseImage', CourseImage);

    CourseImage.$inject = ['$resource'];

    function CourseImage ($resource) {
        console.log("1111111")
        var resourceUrl =  'api/course-images/:id';

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
