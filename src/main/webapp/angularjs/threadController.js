(function() {

    var app = angular.module('myApp');

    app.controller('ThreadController', ['$scope', '$route',
        '$routeParams', 'session', 'AjaxService', 'mainThread', 'threadComments', 'subforumOfThread',
        function($scope, $route, $routeParams, session, AjaxService, mainThread, threadComments, subforumOfThread) {
            $scope.thread = mainThread.data;
            $scope.comments = threadComments.data;
            $scope.subforum = subforumOfThread.data;

            var commentClass = {};

            console.log("Thread kontroler pokrenut " + $routeParams.subforumId + " " + $routeParams.threadId);
            $scope.commentVisible = false;
            $scope.loggedUser = session;


            $scope.checkPermission = function(comment) {

                if ($scope.loggedUser == null)
                    return false;
                if ($scope.loggedUser.role === "ADMIN")
                    return true;
                if ($scope.loggedUser.username === $scope.subforum.mainModId)
                    return true;
                if ($scope.subforum.moderatorsIds.indexOf($scope.loggedUser.username) != -1)
                    return true;
                if ($scope.loggedUser.username === comment.authorId)
                    return true;

                return false;
            }

            $scope.deleteComment = function(comment) {
                delete comment.textEditComment;
                console.log(comment.deleted);
                if (comment.deleted == false) {
                    comment.deleted = true;
                    AjaxService.putResource("subforums/" + $scope.subforum.subForumId + "/threads/" + $scope.thread.nameId + "/comments/" + comment.commentId, comment).then(
                        function(response) {

                            console.log("comment deleted!")

                        },
                        function() {
                            console.log("error on deleted comment");
                        }
                    );
                }
            }

            $scope.editComment = function(comment) {
                comment.content = comment.textEditComment;
                delete comment.textEditComment;
                console.log(JSON.stringify(comment));
                if ($scope.loggedUser.username === $scope.subforum.mainModId) {
                    comment.edited = true;
                }
                AjaxService.putResource("subforums/" + $scope.subforum.subForumId + "/threads/" + $scope.thread.nameId + "/comments/" + comment.commentId, comment).then(
                    function(response) {

                        console.log("comment edited!")

                    },
                    function() {
                        console.log("error on edited comment");
                    }
                );

            }


            $scope.createComment = function(content, parentCommentId) {
                /// when parent = -1, then comment has no parent
                var newComment = {};

                newComment.authorId = $scope.loggedUser.username;

                newComment.parentCommentId = parentCommentId;

                newComment.content = content;

                AjaxService.postResource("subforums/" + $routeParams.subforumId + "/threads/" + $routeParams.threadId + "/comments", newComment).then(
                    function() {
                        console.log("Comment posted successfully");
                        $route.reload();

                    },
                    function() {
                        console.log("Error while posting comment");
                    }
                );
            }

            $scope.getCommentsWithParent = function(parentCommentId) {
                return $scope.comments.filter(comment => comment.parentCommentId === parentCommentId);
            }


            $scope.isVoted = function(likedOrDislikeList) {
                /// glasano je!
                if ($scope.loggedUser == null)
                    return false;

                return likedOrDislikeList.indexOf($scope.loggedUser.username) != -1;
            }



            //// lajkovi


            $scope.dislikeIt = function(item) {

                if ($scope.loggedUser != null) {

                    item.userLikesIds = item.userLikesIds.filter(e => e !== $scope.loggedUser.username);

                    if (item.userDislikeIds.indexOf($scope.loggedUser.username) == -1) {

                        item.userDislikeIds.push($scope.loggedUser.username);
                    } else {
                        item.userDislikeIds = item.userDislikeIds.filter(e => e !== $scope.loggedUser.username);
                    }

                    AjaxService.putResource(item.links[0].link, item).then(
                        function(response) {
                            item = response.data;
                            console.log("uspesno dislike comment");
                        },
                        function() {
                            console.log("error");
                        }
                    )

                }

            }


            $scope.likeIt = function(item) {

                delete item.textEditComment;
                console.log(JSON.stringify(item));

                if ($scope.loggedUser != null) {

                    item.userDislikeIds = item.userDislikeIds.filter(e => e !== $scope.loggedUser.username);

                    if (item.userLikesIds.indexOf($scope.loggedUser.username) == -1) {

                        item.userLikesIds.push($scope.loggedUser.username);
                    } else {
                        item.userLikesIds = item.userLikesIds.filter(e => e !== $scope.loggedUser.username);
                    }

                    AjaxService.putResource(item.links[0].link, item).then(
                        function(response) {
                            item = response.data;
                            console.log("uspesno lajkovan comment");
                        },
                        function() {
                            console.log("error");
                        }
                    )

                }
            }

            $scope.checkIfSaved = function(comment) {

                if ($scope.loggedUser != null) {
                    if ($scope.loggedUser.savedCommentsIds.filter(c => c.commentId === comment.commentId).length > 0) {
                        return true;
                    }
                    return false;
                }

                return false;

            }

            $scope.saveComment = function(comment) {
                delete comment.textEditComment;
                $scope.loggedUser.savedCommentsIds.push(comment);

                AjaxService.putResource("users/" + $scope.loggedUser.username, $scope.loggedUser).then(
                    function(response) {
                        console.log("Successfully saved comment");
                    },
                    function() {
                        console.log("Error while saving comment");
                    }
                )


            }
        }
    ]);
})();