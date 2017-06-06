(function () {
    'use strict';

    angular
        .module('ziranliserverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('exhibition', {
                parent: 'entity',
                url: '/exhibition?page&sort&search',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ziranliserverApp.exhibition.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/exhibition/exhibitions.html',
                        controller: 'ExhibitionController',
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
                        $translatePartialLoader.addPart('exhibition');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('exhibition-detail', {
                parent: 'exhibition',
                url: '/exhibition/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ziranliserverApp.exhibition.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/exhibition/exhibition-detail.html',
                        controller: 'ExhibitionDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('exhibition');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Exhibition', function ($stateParams, Exhibition) {
                        return Exhibition.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'exhibition',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('exhibition-detail.edit', {
                parent: 'exhibition-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/exhibition/exhibition-dialog.html',
                        controller: 'ExhibitionDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Exhibition', function (Exhibition) {
                                return Exhibition.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {reload: false});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('exhibition.new', {
                parent: 'exhibition',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/exhibition/exhibition-dialog.html',
                        controller: 'ExhibitionDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    startDate: null,
                                    endDate: null,
                                    timeDesc: null,
                                    memo: null,
                                    coverImage: null,
                                    qrCode: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('exhibition', null, {reload: 'exhibition'});
                    }, function () {
                        $state.go('exhibition');
                    });
                }]
            })
            .state('exhibition.edit', {
                parent: 'exhibition',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/exhibition/exhibition-dialog.html',
                        controller: 'ExhibitionDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Exhibition', function (Exhibition) {
                                return Exhibition.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('exhibition', null, {reload: 'exhibition'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('exhibition.delete', {
                parent: 'exhibition',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/exhibition/exhibition-delete-dialog.html',
                        controller: 'ExhibitionDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Exhibition', function (Exhibition) {
                                return Exhibition.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('exhibition', null, {reload: 'exhibition'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('exhibition.artworks', {
                parent: 'exhibition',
                url: '/{id}/artworks',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/exhibition/exhibition-artworks-dialog.html',
                        controller: 'ExhibitionArtworksDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Exhibition', function (Exhibition) {
                                return Exhibition.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('exhibition', null, {reload: 'exhibition'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('exhibition.allArtworks', {
                parent: 'exhibition',
                url: '/{id}/allArtworks',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/exhibition/exhibition-chooseArtworks-dialog.html',
                        controller: 'ChooseArtworksController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Exhibition', function (Exhibition) {
                                return Exhibition.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('exhibition', null, {reload: 'exhibition'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            });

    }

})();
