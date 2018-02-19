(function() {

    var app = angular.module('myApp');

    app.factory('AjaxService', function($http) {

        var retVal = {};

        retVal.loggedUser = null;

        retVal.getSession = function() {
            return $http.get("/webforum/webapi/users/getSessionUser");
        }

        retVal.getLoggedUser = function() {
            return retVal.loggedUser;
        }

        retVal.searchResource = function(queryObj) {
            var type = queryObj.typeOf;
            delete queryObj.typeOf;
            var query = type + "/search?" + jQuery.param(queryObj);
            if (type === "threads")
                query = "subforums/e/" + query + "/";
            console.log(query);
            return $http.get('/webforum/webapi/' + query);

        }

        retVal.getResource = function(resourceName) {
            console.log("U servisu " + resourceName);
            return $http.get('/webforum/webapi/' + resourceName);
        }

        retVal.putResource = function(resourceName, object) {
            return $http.put('/webforum/webapi/' + resourceName, object);
        }

        retVal.postResource = function(resourceName, object) {
            return $http.post('/webforum/webapi/' + resourceName, object);
        }

        retVal.deleteResource = function(resourceName) {
            return $http.delete('/webforum/webapi/' + resourceName);
        }

        retVal.addSubforum = function(subforum) {
            var fd = new FormData();
            fd.append('subForumId', subforum.subForumId);
            fd.append('description', subforum.description);
            if (subforum.iconPath != null)
                fd.append('image', subforum.iconPath, subforum.iconPath.name);
            fd.append('mainModId', subforum.mainModId);

            subforum.moderatorsIds.forEach(mod => fd.append('moderatorsIds', mod));
            subforum.listOfRules.forEach(rule => fd.append('listOfRules', rule));

            return $http.post('/webforum/webapi/subforums', fd, {
                transformRequest: angular.identity,
                headers: { 'Content-Type': undefined }
            });

        }

        retVal.addThread = function(thread) {

            var fd = new FormData();

            fd.append('subforumId', thread.subforumId);
            fd.append('nameId', thread.nameId);
            fd.append('type', thread.type);
            fd.append('authorId', thread.authorId);

            if (thread.type === "TEXT") {
                fd.append('text', thread.text);
            } else if (thread.type === "LINK") {
                fd.append('link', thread.link);
            } else if (thread.type === "IMAGE") {
                fd.append('image', thread.image, thread.image.name);
            }

            return $http.post('/webforum/webapi/subforums/' + thread.subforumId + '/threads', fd, {
                transformRequest: angular.identity,
                headers: { 'Content-Type': undefined }
            });

        }

        retVal.editThread = function(thread) {

            var fd = new FormData();

            fd.append('subforumId', thread.subforumId);
            fd.append('nameId', thread.nameId);
            fd.append('type', thread.type);
            fd.append('authorId', thread.authorId);
            fd.append('creationDate', thread.creationDate);

            thread.userLikeIds.forEach(user => fd.append('userLikeIds', user));
            thread.userDislikeIds.forEach(user => fd.append('userDislikeIds', user));

            if (thread.type === "TEXT") {
                fd.append('text', thread.text);
            } else if (thread.type === "LINK") {
                fd.append('link', thread.link);
            } else if (thread.type === "IMAGE") {
                fd.append('image', thread.image, thread.image.name);
            }

            return $http.put('/webforum/webapi/subforums/' + thread.subforumId + '/threads/' + thread.nameId, fd, {
                transformRequest: angular.identity,
                headers: { 'Content-Type': undefined }
            });
        }


        return retVal;

    });





})();