'use strict';

angular.module('controllers', [])

.controller('GuestbookCtrl', ['$scope','$http',function($scope,$http) {
	$scope.sign = {};
	
	$http.get("guestbook/list").success(function(data) {
		$scope.guestbook = data;
	});
	
	$scope.save = function(sign) {
		$http.post("guestbook/save", sign).success(function() {
			$scope.guestbook.push(sign);
			$scope.sign = {};
		}).error(function() {
			alert("Could not save your sign!");
		});
	};
}]);