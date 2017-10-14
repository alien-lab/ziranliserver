/**
 * Created by 鸠小浅 on 2017/10/14.
 */
(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('course-image', {
                parent: 'entity',
                url: '/course-image?page&sort&search',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ziranliserverApp.courseImage.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/course-image/course-images.html',
                        controller: 'CourseImageController',
                        controllerAs: 'vm'
                    }
                },
                params: {
                    page: {
                        value: '1',
                        squash: true
                    },
                    sort: {
                        value: 'id,asc',
                        squash: true
                    },
                    search: null
                },
                resolve: {
                    pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                        return {
                            page: PaginationUtil.parsePage($stateParams.page),
                            sort: $stateParams.sort,
                            predicate: PaginationUtil.parsePredicate($stateParams.sort),
                            ascending: PaginationUtil.parseAscending($stateParams.sort),
                            search: $stateParams.search
                        };
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('courseImage');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('course-image.new', {
                parent: 'course-image',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/course-image/course-image-dialog.html',
                        controller: 'CourseImageDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    image: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function() {
                        $state.go('course-image', null, { reload: 'course-image' });
                    }, function() {
                        $state.go('course-image');
                    });
                }]
            })
            .state('course-image.delete', {
                parent: 'course-image',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/course-image/course-image-delete-dialog.html',
                        controller: 'CourseImageDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['CourseImage', function(CourseImage) {
                                return CourseImage.get({id : $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('course-image', null, { reload: 'course-image' });
                    }, function() {
                        $state.go('^');
                    });
                }]
            });
    }

})();

