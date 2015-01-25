<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no">
    <meta name="keywords" content="itunes, spotify, playlist, playlist, music, transfer">
    <meta name="author" content="Phil MacCart">
    <link href="favicon.ico" rel="shortcut icon" type="image/x-icon"/>
    <title>iTunes2Spotify</title>

    <!-- build:css css/main.css -->
    <link href="css/main.css" rel="stylesheet">
    <link href="bower_components/bootstrap/dist/css/bootstrap.css" rel="stylesheet">
    <!-- endbuild -->

    <!--[if lte IE 8]>
    <script src="bower_components/respond.min.js"></script>
    <script src="bower_components/html5shiv.js"></script>
    <![endif]-->

    <!-- build:js app/vendor.js -->
    <script src="bower_components/angular/angular.js"></script>
    <script src="bower_components/angular-cookies/angular-cookies.js"></script>
    <script src="bower_components/angular-touch/angular-touch.js"></script>
    <script src="bower_components/angular-animate/angular-animate.js"></script>
    <script src="bower_components/angular-sanitize/angular-sanitize.js"></script>
    <script src="bower_components/angular-bootstrap/ui-bootstrap-tpls.js"></script>
    <script src="bower_components/angular-ui-router/release/angular-ui-router.js"></script>
    <script src="bower_components/angular-resource/angular-resource.js"></script>

    <script src="bower_components/lodash/dist/lodash.compat.js"></script>

    <!-- endbuild -->

    <!-- build:js app/index/index.js -->
    <script src="app/app.js"></script>
    <script src="app/pages/home/home.js"></script>
    <script src="app/pages/playlist/playlist.js"></script>
    <script src="app/pages/track/track.js"></script>

    <!-- endbuild -->
</head>
<body ng-app="i2s" ng-controller="AppCtrl" id="ng-app">
<div class="header-wrapper">
    <div ui-view="header"></div>
</div>
<div class="main-content-container container">
    <div ui-view></div>
</div>
<div ui-view="footer"></div>
</body>
</html>
