<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html ng-app="i2s" id="ng-app">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Login</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
</head>
<body ng-controller="ProfileCtrl as profileCtrl">
<h1>Spotify Profile</h1>
<div ng-show="errorMsg" class="alert alert-danger" role="alert" ng-bind="errorMsg"></div>
<div>{{profile | json}}</div>

<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.0/angular.min.js"></script>
<script src="/app/app.js"></script>
</body>
</html>