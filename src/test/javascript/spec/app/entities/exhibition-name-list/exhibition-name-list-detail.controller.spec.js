'use strict';

describe('Controller Tests', function() {

    describe('ExhibitionNameList Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockExhibitionNameList, MockWechatUser, MockExhibition;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockExhibitionNameList = jasmine.createSpy('MockExhibitionNameList');
            MockWechatUser = jasmine.createSpy('MockWechatUser');
            MockExhibition = jasmine.createSpy('MockExhibition');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ExhibitionNameList': MockExhibitionNameList,
                'WechatUser': MockWechatUser,
                'Exhibition': MockExhibition
            };
            createController = function() {
                $injector.get('$controller')("ExhibitionNameListDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ziranliserverApp:exhibitionNameListUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
