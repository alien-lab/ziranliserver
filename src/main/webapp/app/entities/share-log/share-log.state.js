(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('share-log', {
            parent: 'entity',
            url: '/share-log?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ziranliserverApp.shareLog.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/share-log/share-logs.html',
                    controller: 'ShareLogController',
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
                    $translatePartialLoader.addPart('shareLog');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('share-log-detail', {
            parent: 'share-log',
            url: '/share-log/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ziranliserverApp.shareLog.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/share-log/share-log-detail.html',
                    controller: 'ShareLogDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('shareLog');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ShareLog', function($stateParams, ShareLog) {
                    return ShareLog.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'share-log',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('share-log-detail.edit', {
            parent: 'share-log-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/share-log/share-log-dialog.html',
                    controller: 'ShareLogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ShareLog', function(ShareLog) {
                            return ShareLog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('share-log.new', {
            parent: 'share-log',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/share-log/share-log-dialog.html',
                    controller: 'ShareLogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                shareType: null,
                                shareTime: null,
                                shareContentKey: null,
                                shareLink: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('share-log', null, { reload: 'share-log' });
                }, function() {
                    $state.go('share-log');
                });
            }]
        })
        .state('share-log.edit', {
            parent: 'share-log',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/share-log/share-log-dialog.html',
                    controller: 'ShareLogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ShareLog', function(ShareLog) {
                            return ShareLog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('share-log', null, { reload: 'share-log' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('share-log.delete', {
            parent: 'share-log',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/share-log/share-log-delete-dialog.html',
                    controller: 'ShareLogDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ShareLog', function(ShareLog) {
                            return ShareLog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('share-log', null, { reload: 'share-log' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
