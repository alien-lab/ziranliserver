'use strict';

describe('Controller Tests', function() {

    describe('ExhibitionArtwork Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockExhibitionArtwork, MockExhibition, MockArtwork;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockExhibitionArtwork = jasmine.createSpy('MockExhibitionArtwork');
            MockExhibition = jasmine.createSpy('MockExhibition');
            MockArtwork = jasmine.createSpy('MockArtwork');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ExhibitionArtwork': MockExhibitionArtwork,
                'Exhibition': MockExhibition,
                'Artwork': MockArtwork
            };
            createController = function() {
                $injector.get('$controller')("ExhibitionArtworkDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ziranliserverApp:exhibitionArtworkUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
