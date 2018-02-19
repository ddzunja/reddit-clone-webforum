(function() {

    var app = angular.module('myApp');

    app.controller('SubforumsListController', ['$scope', 'AjaxService', 'session', function($scope, AjaxService, session) {
        $scope.results = {};
        $scope.loggedUser = session;



        $scope.getResource = function(resourceName) {

            AjaxService.getResource(resourceName).then(
                function(response) {
                    console.log("Success in getting " + resourceName);
                    $scope.results = response.data;
                },
                function() {
                    console.log("Failed to get " + resourceName);
                }
            );
        }

        $scope.saveSubforum = function(item) {
            if ($scope.loggedUser.subscribedSubForumsIds.indexOf(item.subForumId) == -1) {
                $scope.loggedUser.subscribedSubForumsIds.push(item.subForumId);
                AjaxService.putResource("users/" + $scope.loggedUser.username, $scope.loggedUser).then(
                    function(response) {
                        $scope.loggedUser = response.data;
                    },
                    function() {
                        console.log("error on saving");
                    }
                )


            }
        }

        $scope.unsaveSubforum = function(item) {
            if ($scope.loggedUser.subscribedSubForumsIds.indexOf(item.subForumId) != -1) {
                $scope.loggedUser.subscribedSubForumsIds = $scope.loggedUser.subscribedSubForumsIds
                    .filter(e => e !== item.subForumId);
            }
            AjaxService.putResource("users/" + $scope.loggedUser.username, $scope.loggedUser).then(
                function(response) {
                    $scope.loggedUser = response.data;
                },
                function() {
                    console.log("error on saving");
                }
            )


        }

        $scope.checkIfSaved = function(item) {

            return $scope.loggedUser.subscribedSubForumsIds.indexOf(item.subForumId) != -1;

        }

        $scope.checkSubforumEditDeletePermission = function(subforum) {

            if ($scope.loggedUser != null) {
                if ($scope.loggedUser.role === "ADMIN")
                    return true;

                if ($scope.loggedUser.username === subforum.mainModId)
                    return true;
            }

            return false;
        }

        $scope.checkSubforumAddPermission = function() {

            if ($scope.loggedUser == null)
                return false;

            return ($scope.loggedUser.role === "ADMIN" || $scope.loggedUser.role === "MOD") ? true : false;
        }

        $scope.deleteSubforum = function(subforum) {

            AjaxService.deleteResource(subforum.links[0].link).then(
                function() {

                    console.log("Subforum deleted ");
                    $scope.getResource('subforums');


                },
                function() {}
            );


        }




        $scope.$on("RefreshMainPage", function(event, data) {
            $scope.getResource("subforums");
        });

    }]);




})();