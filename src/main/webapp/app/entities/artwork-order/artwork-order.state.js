(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('artwork-order', {
            parent: 'entity',
            url: '/artwork-order?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ziranliserverApp.artworkOrder.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/artwork-order/artwork-orders.html',
                    controller: 'ArtworkOrderController',
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
                    $translatePartialLoader.addPart('artworkOrder');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('artwork-order-detail', {
            parent: 'artwork-order',
            url: '/artwork-order/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ziranliserverApp.artworkOrder.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/artwork-order/artwork-order-detail.html',
                    controller: 'ArtworkOrderDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('artworkOrder');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ArtworkOrder', function($stateParams, ArtworkOrder) {
                    return ArtworkOrder.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'artwork-order',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('artwork-order-detail.edit', {
            parent: 'artwork-order-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/artwork-order/artwork-order-dialog.html',
                    controller: 'ArtworkOrderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ArtworkOrder', function(ArtworkOrder) {
                            return ArtworkOrder.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('artwork-order.new', {
            parent: 'artwork-order',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/artwork-order/artwork-order-dialog.html',
                    controller: 'ArtworkOrderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                amount: null,
                                payPrice: null,
                                payStatus: null,
                                wechatOrderno: null,
                                orderTime: null,
                                payTime: null,
                                address: null,
                                phone: null,
                                contact: null,
                                orderFlag: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('artwork-order', null, { reload: 'artwork-order' });
                }, function() {
                    $state.go('artwork-order');
                });
            }]
        })
        .state('artwork-order.edit', {
            parent: 'artwork-order',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/artwork-order/artwork-order-dialog.html',
                    controller: 'ArtworkOrderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ArtworkOrder', function(ArtworkOrder) {
                            return ArtworkOrder.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('artwork-order', null, { reload: 'artwork-order' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('artwork-order.delete', {
            parent: 'artwork-order',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/artwork-order/artwork-order-delete-dialog.html',
                    controller: 'ArtworkOrderDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ArtworkOrder', function(ArtworkOrder) {
                            return ArtworkOrder.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('artwork-order', null, { reload: 'artwork-order' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
