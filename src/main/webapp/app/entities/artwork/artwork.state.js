(function () {
    'use strict';

    angular
        .module('ziranliserverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('artwork', {
                parent: 'entity',
                url: '/artwork?page&sort&search',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ziranliserverApp.artwork.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/artwork/artworks.html',
                        controller: 'ArtworkController',
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
                        $translatePartialLoader.addPart('artwork');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('artwork-detail', {
                parent: 'artwork',
                url: '/artwork/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ziranliserverApp.artwork.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/artwork/artwork-detail.html',
                        controller: 'ArtworkDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('artwork');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Artwork', function ($stateParams, Artwork) {
                        return Artwork.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'artwork',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('artwork-detail.edit', {
                parent: 'artwork-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/artwork/artwork-dialog.html',
                        controller: 'ArtworkDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Artwork', function (Artwork) {
                                return Artwork.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {reload: false});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('artwork.new', {
                parent: 'artwork',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/artwork/artwork-dialog.html',
                        controller: 'ArtworkDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    year: null,
                                    author: null,
                                    amount: null,
                                    price: null,
                                    memo: null,
                                    coverImage: null,
                                    status: null,
                                    tags: null,
                                    qrCode: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('artwork', null, {reload: 'artwork'});
                    }, function () {
                        $state.go('artwork');
                    });
                }]
            })
            .state('artwork.edit', {
                parent: 'artwork',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/artwork/artwork-dialog.html',
                        controller: 'ArtworkDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Artwork', function (Artwork) {
                                return Artwork.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('artwork', null, {reload: 'artwork'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('artwork.delete', {
                parent: 'artwork',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/artwork/artwork-delete-dialog.html',
                        controller: 'ArtworkDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Artwork', function (Artwork) {
                                return Artwork.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('artwork', null, {reload: 'artwork'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('artwork.pictures', {
                parent: 'artwork',
                url: '/{id}/pictures',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/artwork/artwork-pictures-dialog.html',
                        controller: 'ArtworkPicDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Artwork', function (Artwork) {
                                return Artwork.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('artwork', null, {reload: 'artwork'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            });
    }

})();
