var app = angular.module('myApp', ['ngRoute', 'checklist-model']);

app.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {

    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/users/:username", {
            templateUrl: "templates/userPageTemplate.html",
            controller: "UserPageController",
            resolve: {
                session: function(AjaxService) {
                    return AjaxService.getSession().then(
                        function(response) {
                            return response.data;
                        },
                        function() {
                            return null;
                        }
                    )
                },
                user: function(AjaxService, $route) {
                    return AjaxService.getResource("users/" + $route.current.params.username).then(
                        function(response) {
                            return response.data;

                        },
                        function() {

                            return null;
                        }
                    )
                }
            }
        })
        .when("/favourites", {
            templateUrl: "templates/homePageTemplate.html",
            controller: "HomePageController",
            resolve: {
                session: function(AjaxService) {
                    return AjaxService.getSession().then(
                        function(response) {
                            return response.data;
                        },
                        function() {
                            return null;
                        }
                    )
                }
            }

        })
        .when("/", {
            templateUrl: "templates/subforumsListPageTemplate.html",
            controller: "SubforumsListController",
            resolve: {
                session: function(AjaxService) {
                    return AjaxService.getSession().then(
                        function(response) {
                            return response.data;
                        },
                        function() {
                            return null;
                        }
                    )
                }
            }
        })
        .when("/signup", {
            templateUrl: "forms/registrationForm.html",
            controller: "RegistrationController",
            resolve: {
                session: function(AjaxService, $location) {
                    return AjaxService.getSession().then(
                        function(response) {
                            $location.path('/');
                            return response.data;
                        },
                        function() {
                            return null;
                        }
                    )
                }
            }
        })
        .when("/subforums/:subforumId", {
            templateUrl: "templates/subforumTemplate.html",
            controller: "SubforumController",
            resolve: {
                session: function(AjaxService) {
                    return AjaxService.getSession().then(
                        function(response) {
                            return response.data;
                        },
                        function() {
                            console.log("no credentials")
                            return null;
                        }
                    )
                }

            }
        })
        .when("/subforums/:subforumId/threads/:threadId", {
            templateUrl: "templates/threadTemplate.html",
            controller: "ThreadController",
            resolve: {

                session: function(AjaxService) {
                    return AjaxService.getSession().then(
                        function(response) {
                            return response.data;
                        },
                        function() {
                            console.log("no credentials")
                            return null;
                        }
                    )
                },
                subforumOfThread: function(AjaxService, $route) {
                    return AjaxService.getResource("subforums/" + $route.current.params.subforumId).then(
                        function(response) {
                            return response;
                        },
                        function() {

                            console.log("error on getting subforum")

                        }
                    );
                },
                mainThread: function(AjaxService, $route) {
                    return AjaxService.getResource("subforums/" + $route.current.params.subforumId + "/threads/" + $route.current.params.threadId).then(
                        function(response) {
                            return response;
                        },
                        function() {
                            console.log("error on getting thread");
                        }
                    );
                },
                threadComments: function(AjaxService, $route) {
                    return AjaxService.getResource("subforums/" + $route.current.params.subforumId + "/threads/" + $route.current.params.threadId + "/comments").then(
                        function(response) {
                            return response;
                        },
                        function() {
                            console.log("error on getting comments");
                        }
                    )
                }


            }
        })
        .when("/messages", {
            templateUrl: "templates/messagesTemplate.html",
            controller: "MessagesController",
            resolve: {
                session: function(AjaxService, $location) {
                    console.log("session resolve");
                    return AjaxService.getSession().then(
                        function(response) {
                            console.log("resolve success");
                            return response;
                        },
                        function() {

                            console.log("Invalid credntials resolver");
                            $location.path("/");
                            $('#loginModal').modal('show');
                        }
                    )
                }
            }
        })
        .when("/search", {
            templateUrl: "templates/searchTemplate.html",
            controller: "SearchController",
            resolve: {
                searchResults: function(AjaxService, $location) {
                    var query = angular.copy($location.search());
                    return AjaxService.searchResource(query).then(
                        function(response) {
                            return response;
                        },
                        function() {
                            console.log("error in searchController");
                        }
                    )

                }
            }
        });

}]);

app.run(['$rootScope', '$location', 'AjaxService', function($rootScope, $location, AjaxService) {

    $rootScope.$watch('loggedUser', function() {
        $rootScope.$broadcast('userLoginChange', $rootScope.loggedUser);
    }, true);

    /// kada god se promeni ruta, proveri usera!
    $rootScope.$on('$routeChangeStart', function(event, next, current) {

        $rootScope.loggedUser = null;

        AjaxService.getResource("users/getSessionUser").then(
            function(response) {
                console.log("Credentials success");
                $rootScope.loggedUser = response.data;

            },
            function() {
                console.log("No credentials");
                $rootScope.loggedUser = null;

            }
        );
    });




}]);