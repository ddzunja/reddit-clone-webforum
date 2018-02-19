(function() {

    var app = angular.module('myApp');

    app.controller('SubforumController', ['$scope', '$routeParams', 'AjaxService', 'session', function($scope, $routeParams, AjaxService, session) {
        $scope.subforum = [];
        $scope.threads = {};
        $scope.editDeletePermission = false;

        $scope.loggedUser = session;
        // $scope.$on('userLoginChange', function(events, user) {
        //     $scope.loggedUser = user;
        //     $scope.permissions();
        // });

        $scope.permissions = function() {
            if ($scope.loggedUser == null)
                $scope.editDeletePermission = false;
            else if ($scope.loggedUser.role === "ADMIN" || $scope.loggedUser.username === $scope.subforum.mainModId ||
                $scope.subforum.moderatorsIds.indexOf($scope.loggedUser.username) != -1)

                $scope.editDeletePermission = true;
            else $scope.editDeletePermission = false;

        }

        $scope.getSubforum = function() {
            AjaxService.getResource("subforums/" + $routeParams.subforumId).then(
                function(response) {
                    $scope.subforum = response.data;
                    console.log("Subforum " + JSON.stringify($scope.subforum));

                    $scope.permissions();
                },
                function() {

                    console.log("Error in getting subforum");
                }
            );
        }

        $scope.getThreads = function() {

            AjaxService.getResource("subforums/" + $routeParams.subforumId + "/threads").then(
                function(response) {
                    $scope.threads = response.data;
                    console.log("Subforum " + JSON.stringify($scope.threads));

                },
                function() {

                    console.log("Error in getting threads");
                }
            );
        }

        $scope.getResourceInit = function() {

            $scope.getSubforum();
            $scope.getThreads();

        }

        $scope.deleteThread = function(thread) {

            AjaxService.deleteResource(thread.links[0].link).then(
                function(response) {

                    console.log("Thread deleted succesfully");
                    $scope.getThreads();

                },
                function() {

                    console.log("Error on deleting thread");
                }
            );

        }

        $scope.editThread = function(thread) {

            $scope.$broadcast("EditThread", thread);

        }

        $scope.createThread = function() {
            $scope.$broadcast("CreateThread");
        }


        $scope.isVoted = function(likedOrDislikeList) {
            /// glasano je!
            if ($scope.loggedUser == null)
                return false;

            return likedOrDislikeList.indexOf($scope.loggedUser.username) != -1;
        }

        $scope.dislikeIt = function(item) {

            if ($scope.loggedUser != null) {

                item.userLikeIds = item.userLikeIds.filter(e => e !== $scope.loggedUser.username);

                if (item.userDislikeIds.indexOf($scope.loggedUser.username) == -1) {

                    item.userDislikeIds.push($scope.loggedUser.username);
                } else {
                    item.userDislikeIds = item.userDislikeIds.filter(e => e !== $scope.loggedUser.username);
                }

                AjaxService.putResource(item.links[0].link + "/votes", item).then(
                    function(response) {
                        item = response.data;
                        console.log("uspesno dislike thread");
                    },
                    function() {
                        console.log("error");
                    }
                )

            }

        }


        $scope.likeIt = function(item) {

            ///.filter(e => e !== item.subForumId);
            if ($scope.loggedUser != null) {

                item.userDislikeIds = item.userDislikeIds.filter(e => e !== $scope.loggedUser.username);

                if (item.userLikeIds.indexOf($scope.loggedUser.username) == -1) {

                    item.userLikeIds.push($scope.loggedUser.username);
                } else {
                    item.userLikeIds = item.userLikeIds.filter(e => e !== $scope.loggedUser.username);
                }

                AjaxService.putResource(item.links[0].link + "/votes", item).then(
                    function(response) {
                        item = response.data;
                        console.log("uspesno lajkovan thread");
                    },
                    function() {
                        console.log("error");
                    }
                )

            }
        }

        $scope.isSavedThread = function(thread) {

            if ($scope.loggedUser.subscribedThreadsIds.filter(t => t.nameId === thread.nameId && t.date === thread.date).length > 0) {
                return true;
            }

            return false;
        }

        $scope.saveThread = function(thread) {
            if ($scope.loggedUser != null) {

                $scope.loggedUser.subscribedThreadsIds.push(thread);
                console.log("saving thread");
                AjaxService.putResource("users/" + $scope.loggedUser.username, $scope.loggedUser).then(
                    function(response) {
                        console.log("User updated!");
                    },
                    function() {
                        console.log("Error while updating user");
                    }
                )

            }
        }

        $scope.$on("RefreshSubforumPage", function(event, args) {
            $scope.getThreads();
        });


    }]);
})();