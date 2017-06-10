/**
 * Created by 橘 on 2017/6/9.
 */
(function(){
    'use strict';
    angular
        .module('ziranliserverApp')
        .directive("onliveSelector",onliveSelector);
    onliveSelector.$inject = ['onliveService'];
    function onliveSelector(onliveService){
        var directive = {
            restrict: 'EA',
            templateUrl:'app/entities/course/onliveselector.html',
            scope:{
              courseid:"=",onliveid:"="
            },
            link: linkFunc
        };

        return directive;

        function linkFunc (scope, element, attrs, formCtrl) {
            scope.currentOnlive=null;
            scope.onlivelist=null;
            if(scope.courseid){
                onliveService.loadCourseOnlive(scope.courseid,function(data){
                    var onlive=data.data;
                    if(onlive){
                        scope.currentOnlive=onlive;
                        console.log(scope.currentOnlive);
                    }
                })
            }
            loadRoom();
            function loadRoom(){
                onliveService.loadAllOnliveRoom(function(data,flag){
                    if(!flag){
                        return;
                    }
                    scope.onlivelist=data.data;
                })
            }


            scope.addOnlive=function(onlive){
                scope.currentOnlive=onlive;
                scope.onliveid=onlive.bc_no;
                if(scope.courseid){
                    onliveService.addOnlive(scope.courseid,scope.onliveid,function(data){
                        console.log(data);
                    })
                }
            }
            scope.deleteOnlive=function(onlive){
                if(scope.courseid){
                    onliveService.delOnlive(scope.courseid,onlive.bc_no,function(data){
                        scope.currentOnlive=undefined;
                        loadRoom();
                    })
                }
            }
        }
    }

})();
(function(){
    'use strict';
    angular
        .module('ziranliserverApp')
        .service("onliveService",["$http","domain",function($http,domain){
            this.loadAllOnliveRoom=function(callback){
                $http({
                    url:"api/courses/onlive",
                    method:"GET"
                }).then(function(result){
                    if(callback){
                        callback(result,true);
                    }
                },function(result){
                    if(callback){
                        callback(result,false);
                    }
                });
            }
            this.loadCourseOnlive=function(courseId,callback){
                $http({
                    url:"api/courses/onlive/"+courseId,
                    method:"GET"
                }).then(function(result){
                    if(callback){
                        callback(result,true);
                    }
                },function(result){
                    if(callback){
                        callback(result,false);
                    }
                });
            }

            this.addOnlive=function(courseId,onliveId,callback){
                $http({
                    url:"api/courses/onlive",
                    method:"POST",
                    data:{
                        courseId:courseId,
                        onliveId:onliveId
                    }
                }).then(function(result){
                    if(callback){
                        callback(result,true);
                    }
                },function(result){
                    if(callback){
                        callback(result,false);
                    }
                });
            }

            this.delOnlive=function(courseId,onliveId,callback){
                $http({
                    url:"api/courses/onlive/"+courseId+"/"+onliveId,
                    method:"DELETE"
                }).then(function(result){
                    if(callback){
                        callback(result,true);
                    }
                },function(result){
                    if(callback){
                        callback(result,false);
                    }
                });
            }
        }]);
})();
