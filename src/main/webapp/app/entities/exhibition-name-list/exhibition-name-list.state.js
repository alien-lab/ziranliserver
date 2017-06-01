(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('exhibition-name-list', {
            parent: 'entity',
            url: '/exhibition-name-list?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ziranliserverApp.exhibitionNameList.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/exhibition-name-list/exhibition-name-lists.html',
                    controller: 'ExhibitionNameListController',
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
                    $translatePartialLoader.addPart('exhibitionNameList');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('exhibition-name-list-detail', {
            parent: 'exhibition-name-list',
            url: '/exhibition-name-list/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ziranliserverApp.exhibitionNameList.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/exhibition-name-list/exhibition-name-list-detail.html',
                    controller: 'ExhibitionNameListDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('exhibitionNameList');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ExhibitionNameList', function($stateParams, ExhibitionNameList) {
                    return ExhibitionNameList.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'exhibition-name-list',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('exhibition-name-list-detail.edit', {
            parent: 'exhibition-name-list-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/exhibition-name-list/exhibition-name-list-dialog.html',
                    controller: 'ExhibitionNameListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ExhibitionNameList', function(ExhibitionNameList) {
                            return ExhibitionNameList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('exhibition-name-list.new', {
            parent: 'exhibition-name-list',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/exhibition-name-list/exhibition-name-list-dialog.html',
                    controller: 'ExhibitionNameListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                joinTime: null,
                                signTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('exhibition-name-list', null, { reload: 'exhibition-name-list' });
                }, function() {
                    $state.go('exhibition-name-list');
                });
            }]
        })
        .state('exhibition-name-list.edit', {
            parent: 'exhibition-name-list',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/exhibition-name-list/exhibition-name-list-dialog.html',
                    controller: 'ExhibitionNameListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ExhibitionNameList', function(ExhibitionNameList) {
                            return ExhibitionNameList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('exhibition-name-list', null, { reload: 'exhibition-name-list' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('exhibition-name-list.delete', {
            parent: 'exhibition-name-list',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/exhibition-name-list/exhibition-name-list-delete-dialog.html',
                    controller: 'ExhibitionNameListDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ExhibitionNameList', function(ExhibitionNameList) {
                            return ExhibitionNameList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('exhibition-name-list', null, { reload: 'exhibition-name-list' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
