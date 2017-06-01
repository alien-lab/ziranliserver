(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('course-order', {
            parent: 'entity',
            url: '/course-order?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ziranliserverApp.courseOrder.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/course-order/course-orders.html',
                    controller: 'CourseOrderController',
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
                    $translatePartialLoader.addPart('courseOrder');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('course-order-detail', {
            parent: 'course-order',
            url: '/course-order/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ziranliserverApp.courseOrder.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/course-order/course-order-detail.html',
                    controller: 'CourseOrderDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('courseOrder');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CourseOrder', function($stateParams, CourseOrder) {
                    return CourseOrder.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'course-order',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('course-order-detail.edit', {
            parent: 'course-order-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/course-order/course-order-dialog.html',
                    controller: 'CourseOrderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CourseOrder', function(CourseOrder) {
                            return CourseOrder.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('course-order.new', {
            parent: 'course-order',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/course-order/course-order-dialog.html',
                    controller: 'CourseOrderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                payPrice: null,
                                payTime: null,
                                isShare: null,
                                payStatus: null,
                                wechatOrderno: null,
                                orderTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('course-order', null, { reload: 'course-order' });
                }, function() {
                    $state.go('course-order');
                });
            }]
        })
        .state('course-order.edit', {
            parent: 'course-order',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/course-order/course-order-dialog.html',
                    controller: 'CourseOrderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CourseOrder', function(CourseOrder) {
                            return CourseOrder.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('course-order', null, { reload: 'course-order' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('course-order.delete', {
            parent: 'course-order',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/course-order/course-order-delete-dialog.html',
                    controller: 'CourseOrderDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CourseOrder', function(CourseOrder) {
                            return CourseOrder.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('course-order', null, { reload: 'course-order' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
