'use strict';

describe('Controller Tests', function() {

    describe('ShareLog Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockShareLog, MockWechatUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockShareLog = jasmine.createSpy('MockShareLog');
            MockWechatUser = jasmine.createSpy('MockWechatUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ShareLog': MockShareLog,
                'WechatUser': MockWechatUser
            };
            createController = function() {
                $injector.get('$controller')("ShareLogDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ziranliserverApp:shareLogUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
