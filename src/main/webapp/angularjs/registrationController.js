(function() {

    var app = angular.module('myApp');

    app.controller('RegistrationController', ['$scope', 'AjaxService', 'session', '$location', function($scope, AjaxService, session, $location) {

        $scope.loggedUser = session;
        console.log("sign up");

        $scope.newUser = { username: '', password: '', name: '', phone: '', email: '' };

        $scope.addNewUser = function(newUser) {
            newUser.username = newUser.username.toLowerCase();

            AjaxService.postResource('users', newUser).then(
                function(response) {

                    console.log("New user " + newUser.username);
                    $location.path("/");
                },
                function() {

                    console.log("Faild to add\n Conflict probably");
                }
            )
        }
    }]);

})();