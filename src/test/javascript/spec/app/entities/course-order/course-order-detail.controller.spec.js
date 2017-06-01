'use strict';

describe('Controller Tests', function() {

    describe('CourseOrder Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockCourseOrder, MockWechatUser, MockCourse;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockCourseOrder = jasmine.createSpy('MockCourseOrder');
            MockWechatUser = jasmine.createSpy('MockWechatUser');
            MockCourse = jasmine.createSpy('MockCourse');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'CourseOrder': MockCourseOrder,
                'WechatUser': MockWechatUser,
                'Course': MockCourse
            };
            createController = function() {
                $injector.get('$controller')("CourseOrderDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ziranliserverApp:courseOrderUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
