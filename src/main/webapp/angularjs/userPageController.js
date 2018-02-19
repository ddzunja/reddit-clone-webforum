(function() {

    var app = angular.module('myApp');

    app.controller("UserPageController", ['$scope', 'session', 'user', 'AjaxService', function($scope, session, user, AjaxService) {

        $scope.loggedUser = session;
        $scope.displayedUser = user;
        $scope.editMode = false;

        var userClass = { name: '', role: '', phone: '', email: '', date: '' };

        $scope.editUser = angular.copy(userClass);

        $scope.editPermission = function() {
            if ($scope.loggedUser == null) {
                return false;
            }
            if ($scope.displayedUser.username == $scope.loggedUser.username) {
                return true;
            }
            if ($scope.loggedUser.role == 'ADMIN') {
                return true;
            }

            return false;


        }

        $scope.editUser = function() {
            $scope.editUser = angular.copy($scope.displayedUser);
        }

        $scope.saveEditedUser = function() {
            $scope.displayedUser.name = $scope.editUser.name;
            $scope.displayedUser.password = $scope.editUser.password;
            $scope.displayedUser.role = $scope.editUser.role;
            $scope.displayedUser.phone = $scope.editUser.phone;
            $scope.displayedUser.email = $scope.editUser.email;

            $scope.editUser = angular.copy(userClass);
            $('#editModal').modal('toggle');

            console.log("USER EDIT" + $scope.displayedUser);

            AjaxService.putResource("users/" + $scope.displayedUser.username, $scope.displayedUser).then(
                function(response) {
                    $scope.displayedUser = response.data;
                    console.log("Edited user");
                },
                function() {

                    console.log("User edit failer")
                }
            )



        }

    }]);
})();