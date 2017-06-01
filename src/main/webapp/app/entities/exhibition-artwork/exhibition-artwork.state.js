(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('exhibition-artwork', {
            parent: 'entity',
            url: '/exhibition-artwork?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ziranliserverApp.exhibitionArtwork.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/exhibition-artwork/exhibition-artworks.html',
                    controller: 'ExhibitionArtworkController',
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
                    $translatePartialLoader.addPart('exhibitionArtwork');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('exhibition-artwork-detail', {
            parent: 'exhibition-artwork',
            url: '/exhibition-artwork/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ziranliserverApp.exhibitionArtwork.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/exhibition-artwork/exhibition-artwork-detail.html',
                    controller: 'ExhibitionArtworkDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('exhibitionArtwork');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ExhibitionArtwork', function($stateParams, ExhibitionArtwork) {
                    return ExhibitionArtwork.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'exhibition-artwork',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('exhibition-artwork-detail.edit', {
            parent: 'exhibition-artwork-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/exhibition-artwork/exhibition-artwork-dialog.html',
                    controller: 'ExhibitionArtworkDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ExhibitionArtwork', function(ExhibitionArtwork) {
                            return ExhibitionArtwork.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('exhibition-artwork.new', {
            parent: 'exhibition-artwork',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/exhibition-artwork/exhibition-artwork-dialog.html',
                    controller: 'ExhibitionArtworkDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                amount: null,
                                price: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('exhibition-artwork', null, { reload: 'exhibition-artwork' });
                }, function() {
                    $state.go('exhibition-artwork');
                });
            }]
        })
        .state('exhibition-artwork.edit', {
            parent: 'exhibition-artwork',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/exhibition-artwork/exhibition-artwork-dialog.html',
                    controller: 'ExhibitionArtworkDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ExhibitionArtwork', function(ExhibitionArtwork) {
                            return ExhibitionArtwork.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('exhibition-artwork', null, { reload: 'exhibition-artwork' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('exhibition-artwork.delete', {
            parent: 'exhibition-artwork',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/exhibition-artwork/exhibition-artwork-delete-dialog.html',
                    controller: 'ExhibitionArtworkDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ExhibitionArtwork', function(ExhibitionArtwork) {
                            return ExhibitionArtwork.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('exhibition-artwork', null, { reload: 'exhibition-artwork' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
