(function() {
    'use strict';

    angular
        .module('ziranliserverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('artwork-image', {
            parent: 'entity',
            url: '/artwork-image?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ziranliserverApp.artworkImage.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/artwork-image/artwork-images.html',
                    controller: 'ArtworkImageController',
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
                    $translatePartialLoader.addPart('artworkImage');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('artwork-image-detail', {
            parent: 'artwork-image',
            url: '/artwork-image/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ziranliserverApp.artworkImage.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/artwork-image/artwork-image-detail.html',
                    controller: 'ArtworkImageDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('artworkImage');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ArtworkImage', function($stateParams, ArtworkImage) {
                    return ArtworkImage.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'artwork-image',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('artwork-image-detail.edit', {
            parent: 'artwork-image-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/artwork-image/artwork-image-dialog.html',
                    controller: 'ArtworkImageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ArtworkImage', function(ArtworkImage) {
                            return ArtworkImage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('artwork-image.new', {
            parent: 'artwork-image',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/artwork-image/artwork-image-dialog.html',
                    controller: 'ArtworkImageDialogController',
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
                    $state.go('artwork-image', null, { reload: 'artwork-image' });
                }, function() {
                    $state.go('artwork-image');
                });
            }]
        })
        .state('artwork-image.edit', {
            parent: 'artwork-image',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/artwork-image/artwork-image-dialog.html',
                    controller: 'ArtworkImageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ArtworkImage', function(ArtworkImage) {
                            return ArtworkImage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('artwork-image', null, { reload: 'artwork-image' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('artwork-image.delete', {
            parent: 'artwork-image',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/artwork-image/artwork-image-delete-dialog.html',
                    controller: 'ArtworkImageDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ArtworkImage', function(ArtworkImage) {
                            return ArtworkImage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('artwork-image', null, { reload: 'artwork-image' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
