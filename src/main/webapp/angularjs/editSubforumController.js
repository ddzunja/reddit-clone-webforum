(function() {

    var app = angular.module('myApp');

    app.controller('editSubforumController', ['$scope', 'AjaxService', function($scope, AjaxService) {

        $('#subforumIdExists').hide();
        var subforumClass = { subForumId: '', description: '', iconPath: '', listOfRules: {}, mainModId: '', moderatorsIds: {} };
        $scope.moderators = {};
        $scope.newSubforum = angular.copy(subforumClass);

        $scope.editClose = function() {

            $scope.newSubforum = angular.copy(subforumClass);
            $('#subforumIdExists').hide();
            $('#newSubforumModal').modal('toggle');
        }


        $scope.getUsersMod = function() {
            AjaxService.getResource("users/getAllModsAdmins").then(
                function(response) {
                    $scope.moderators = response.data;
                    console.log("Moderatori " + $scope.moderators);
                }
            );
        }

        $scope.createSubforum = function() {

            $scope.newSubforum.mainModId = $scope.$parent.loggedUser.username;

            $scope.newSubforum.iconPath = $('#imgSubforum').prop('files')[0];
            AjaxService.addSubforum($scope.newSubforum).then(
                function(response) {

                    console.log("Forum added succesfully");
                    $scope.$emit("RefreshMainPage");
                    $('#newSubforumModal').modal('toggle');
                    $scope.newSubforum = angular.copy(subforumClass);
                },
                function() {

                    $('#subforumIdExists').show();
                    console.log("Error while adding subforum");

                }
            );

        }










    }]);
})();