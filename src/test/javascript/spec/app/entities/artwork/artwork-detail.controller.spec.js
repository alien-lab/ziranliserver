'use strict';

describe('Controller Tests', function() {

    describe('Artwork Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockArtwork, MockArtworkImage;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockArtwork = jasmine.createSpy('MockArtwork');
            MockArtworkImage = jasmine.createSpy('MockArtworkImage');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Artwork': MockArtwork,
                'ArtworkImage': MockArtworkImage
            };
            createController = function() {
                $injector.get('$controller')("ArtworkDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ziranliserverApp:artworkUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
