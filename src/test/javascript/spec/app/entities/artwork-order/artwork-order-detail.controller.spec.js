'use strict';

describe('Controller Tests', function() {

    describe('ArtworkOrder Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockArtworkOrder, MockArtwork, MockWechatUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockArtworkOrder = jasmine.createSpy('MockArtworkOrder');
            MockArtwork = jasmine.createSpy('MockArtwork');
            MockWechatUser = jasmine.createSpy('MockWechatUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ArtworkOrder': MockArtworkOrder,
                'Artwork': MockArtwork,
                'WechatUser': MockWechatUser
            };
            createController = function() {
                $injector.get('$controller')("ArtworkOrderDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ziranliserverApp:artworkOrderUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
