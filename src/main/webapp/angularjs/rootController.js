(function() {

    var app = angular.module('myApp');

    app.controller('RootController', ['$rootScope', '$scope', '$location', 'AjaxService', '$route', function($rootScope, $scope, $location, AjaxService, $route) {

        var userSearchClass = { typeOf: 'users', username: '' };
        var subforumSearchClass = { typeOf: 'subforums', subforumId: '', description: '', mainModId: '' };
        var threadSearchClass = { typeOf: 'threads', nameId: '', type: '', authorId: '', subforumId: '' };


        $scope.userSearch = angular.copy(userSearchClass);
        $scope.subforumSearch = angular.copy(subforumSearchClass);
        $scope.threadSearch = angular.copy(threadSearchClass);
        $scope.typeOfSearch = "";
        $scope.name = "";

        $scope.searchObject = function() {
            $('#searchModal').modal('toggle');

            var toSearch = {};

            if ($scope.typeOfSearch === "users") {
                toSearch = $scope.userSearch;

            } else if ($scope.typeOfSearch === "subforums") {
                toSearch = $scope.subforumSearch;

            } else if ($scope.typeOfSearch === "threads") {
                toSearch = $scope.threadSearch;
            }

            $scope.userSearch = angular.copy(userSearchClass);
            $scope.subforumSearch = angular.copy(subforumSearchClass);
            $scope.threadSearch = angular.copy(threadSearchClass);
            $scope.typeOfSearch = "";


            $location.url("/search").search(toSearch);
        }


        $scope.$on('userLoginChange', function(events, user) {
            if (user != null) {
                $scope.name = user.name;
                $scope.loggedIn = true;
            } else {
                $scope.name = "";
                $scope.loggedIn = false;
            }

        });

        //pravi sesiju
        $scope.loginUser = function(credentials) {
            AjaxService.postResource('users/login', credentials).then(
                function(response) {

                    // Notify watch!
                    $rootScope.loggedUser = response.data;
                    $scope.name = $rootScope.loggedUser.name;

                    // UI stuff;
                    console.log("User " + $rootScope.loggedUser.username + " successful login");
                    $('#loginModal').modal('hide');
                    $('#wrongUserPass').addClass('hidden');
                    $route.reload();
                    /// reset login form
                    credentials.username = "";
                    credentials.password = "";
                },
                function() {

                    $rootScope.loggedUser = null;
                    console.log("Bad credentials!");
                    $('#wrongUserPass').removeClass('hidden');
                }
            )
        }

        $scope.logoutUser = function() {

            AjaxService.getResource("users/logout").then(
                function(response) {
                    console.log("log out");
                    $rootScope.loggedUser = null;
                    AjaxService.loggedUser = null;
                    // $location.path('/');
                    $route.reload();

                },
                function() {
                    console.log("Somethings wrong!");
                }
            )
        }


    }]);

})();