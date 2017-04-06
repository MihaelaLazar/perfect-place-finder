angular.module('projectOnlineStore', ['ngRoute'])
        .controller('UserController', ['$scope', '$http', function ($scope, $http) {
            $scope.user = {};
            $scope.results = " pedala"

            $scope.apasaPedala = function() {
                 $http.get('/apasa',
                     function (response) {
                     console.log("iei");
                     $scope.results = response; },
                     function (failure) { console.log("failed :(", failure); });
            }

            $scope.register = function () {
                 $http({
                         url: 'localhost:8080/user/register',
                         method: "POST",
                         data: { 'user' : $scope.user },
                         headers: {'Content-Type': 'application/json'}
                     })
                     .then(function(response) {
                              $scope.results = response;
                     },
                     function(response) { // optional
                             console.log("failed :(", failure);
                     });

             //   $http.post('http://localhost:8080/user/register', { params: user },
            //      function (response) { $scope.results = response; },
             //     function (failure) { console.log("failed :(", failure); });
            }
        }]);