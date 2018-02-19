(function() {

    var app = angular.module('myApp');

    app.controller('MessagesController', ['$scope', 'AjaxService', '$location', 'session', '$route', function($scope, AjaxService, $location, session, $route) {

            var MessageClass = { sender: '', receiver: '', content: '', seen: false };

            $scope.newMessage = angular.copy(MessageClass);

            $scope.sender = {};
            $scope.receiver = {};
            $scope.users = {};
            $scope.loggedUser = session.data;
            $scope.selected = "inbox";

            $scope.createNewMessage = function() {

                $scope.newMessage.sender = $scope.loggedUser.username;

                AjaxService.postResource("messages/send", $scope.newMessage).then(
                    function(response) {
                        console.log("Message sent");
                        $scope.newMessage = angular.copy(MessageClass);
                        $scope.init();
                    },
                    function() {
                        console.log("Error");
                    }
                )


            }


            $scope.messageSeen = function(message) {

                if (message.seen == false) {
                    message.seen = true;
                    AjaxService.putResource("messages/seen", message).then(
                        function(response) {
                            console.log("Success on updateing message");
                        },
                        function() {
                            console.log("ERROR on updating message");
                        }
                    );
                }


            }

            $scope.init = function() {


                AjaxService.getResource("messages/sender/" + $scope.loggedUser.username).then(
                    function(response) {
                        console.log("Ajax poziv sender");
                        $scope.sender = response.data;

                    },
                    function() {
                        console.log("error on getting sender messages");

                    }


                );

                AjaxService.getResource("messages/receiver/" + $scope.loggedUser.username).then(
                    function(response) {
                        console.log("Ajax poziv receiver");

                        $scope.receiver = response.data;

                    },
                    function() {
                        console.log("error on getting sender messages");

                    }
                );

                AjaxService.getResource("users").then(
                    function(response) {
                        console.log("Ajax poziv user");
                        $scope.users = response.data;

                    },
                    function() {
                        console.log("error on getting users");

                    }
                )

            }




        }


    ]);
})();