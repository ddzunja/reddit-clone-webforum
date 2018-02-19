(function() {

    var app = angular.module('myApp');

    app.controller("SearchController", ['$scope', 'AjaxService', '$location', 'searchResults', function($scope, AjaxService, $location, searchResults) {

        $scope.results = searchResults.data;
        $scope.type = $location.search().typeOf;




    }]);
})();