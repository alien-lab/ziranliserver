(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('course-type', {
            parent: 'entity',
            url: '/course-type?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ziranliserverApp.courseType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/course-type/course-types.html',
                    controller: 'CourseTypeController',
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
                    $translatePartialLoader.addPart('courseType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('course-type-detail', {
            parent: 'course-type',
            url: '/course-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ziranliserverApp.courseType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/course-type/course-type-detail.html',
                    controller: 'CourseTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('courseType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CourseType', function($stateParams, CourseType) {
                    return CourseType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'course-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('course-type-detail.edit', {
            parent: 'course-type-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/course-type/course-type-dialog.html',
                    controller: 'CourseTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CourseType', function(CourseType) {
                            return CourseType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('course-type.new', {
            parent: 'course-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/course-type/course-type-dialog.html',
                    controller: 'CourseTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('course-type', null, { reload: 'course-type' });
                }, function() {
                    $state.go('course-type');
                });
            }]
        })
        .state('course-type.edit', {
            parent: 'course-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/course-type/course-type-dialog.html',
                    controller: 'CourseTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CourseType', function(CourseType) {
                            return CourseType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('course-type', null, { reload: 'course-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('course-type.delete', {
            parent: 'course-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/course-type/course-type-delete-dialog.html',
                    controller: 'CourseTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CourseType', function(CourseType) {
                            return CourseType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('course-type', null, { reload: 'course-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
