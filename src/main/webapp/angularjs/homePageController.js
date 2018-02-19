(function() {

    var app = angular.module('myApp');

    app.controller("HomePageController", ['$scope', 'session', '$q', 'AjaxService', function($scope, session, $q, AjaxService) {

        $scope.loggedUser = session;


        $scope.init = function() {

            var ajaxCall = function(subName) {
                var deferred = $q.defer();

                AjaxService.getResource("subforums/" + subName + "/threads").then(
                    function(response) {
                        deferred.resolve(response.data);
                    },
                    function(error) {
                        deferred.reject([]);
                    }
                );

                return deferred.promise;
            }

            if (session == null) {
                return null;
            }

            var promises = [];

            session.subscribedSubForumsIds.forEach(sub => promises.push(ajaxCall(sub)));

            $q.all(promises).then(
                function(responses) {

                    $scope.subforumThreads = responses;
                    $scope.allThreads = [];

                    responses.forEach(response => response.forEach(thread => $scope.allThreads.push(thread)));
                    $scope.allThreads.sort(function compare(t1, t2) {
                        if ((t2.userLikeIds.length - t2.userDislikeIds.length) > (t1.userLikeIds.length - t1.userDislikeIds.length)) {
                            return 1;
                        }

                        if ((t2.userLikeIds.length - t2.userDislikeIds.length) < (t1.userLikeIds.length - t1.userDislikeIds.length)) {
                            return -1;
                        }

                        if ((t2.userLikeIds.length - t2.userDislikeIds.length) == (t1.userLikeIds.length - t1.userDislikeIds.length)) {
                            return 0;
                        }
                    });
                }
            );


        }













    }]);
})();