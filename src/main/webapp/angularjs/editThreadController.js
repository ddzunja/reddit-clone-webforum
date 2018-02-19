(function() {

    var app = angular.module('myApp');

    app.controller('EditThreadController', ['$scope', 'AjaxService', function($scope, AjaxService) {

        var threadClass = { subforumId: '', nameId: '', type: '', authorId: '', comments: {}, link: '', text: '', image: [], creationDate: '', userLikeIds: {}, userDislikeIds: {} };
        $('#threadIdExists').hide();
        $scope.newThread = angular.copy(threadClass);
        $scope.editMode = false;

        $scope.editClose = function() {

            $scope.newThread = angular.copy(threadClass);
            $('#threadIdExists').hide();
            $('#newThreadModal').modal('toggle');

        }

        $scope.$on("CreateThread", function(event) {
            $scope.editMode = false;
            $('#threadIdExists').hide();
            $('#newThreadModal').modal('toggle');
            $scope.newThread = angular.copy(threadClass);

        })

        $scope.$on("EditThread", function(event, thread) {
            $scope.editMode = true;
            console.log(JSON.stringify(thread));
            $scope.newThread = angular.copy(thread);

            if (thread.type === "TEXT") {
                $scope.newThread.text = thread.content;
            } else if (thread.type === "LINK") {
                $scope.newThread.link = thread.content;
            } else if (thread.type === "IMAGE") {
                $scope.newThread.image = thread.content;
                $('#img-preview-thread').attr("src", thread.content);
                ///setuj vrednost input type=file na vrednost
                /// vrati src na normalno ukoliko zatvori dijalog
            }

            $('#threadIdExists').hide();
            $('#newThreadModal').modal('toggle');


        })

        $scope.createThread = function() {
            $scope.editMode = false;
            if ($scope.newThread.type === "IMAGE") {
                $scope.newThread.image = $('#imgThread').prop('files')[0];
            }

            $scope.newThread.authorId = $scope.$parent.loggedUser.username;
            $scope.newThread.subforumId = $scope.$parent.subforum.subForumId;


            AjaxService.addThread($scope.newThread).then(
                function(response) {

                    $scope.$emit("RefreshSubforumPage");
                    $scope.newThread = angular.copy(threadClass);
                    $('#newThreadModal').modal('toggle');

                },
                function() {
                    $('#threadIdExists').show();
                    console.log("error on creating thread");
                }
            );
        }

        $scope.saveEditedThread = function() {

            if ($scope.newThread.type === "IMAGE") {
                $scope.newThread.image = $('#imgThread').prop('files')[0]; // proveri da li se file vezuje
            }

            AjaxService.editThread($scope.newThread).then(
                function(response) {
                    $scope.$emit("RefreshSubforumPage");
                    $scope.newThread = angular.copy(threadClass);
                    $('#newThreadModal').modal('toggle');
                },
                function() {
                    console.log("error");

                }
            );

        }









    }]);

})();