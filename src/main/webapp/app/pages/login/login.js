angular.module('i2s.login', [
  'i2s'
])
  .config(function ($urlRouterProvider, $stateProvider) {
    $urlRouterProvider.otherwise('/');
    $stateProvider
      .state('login', {
        url: '/',
        views: {
          login: {
            templateUrl: 'app/pages/login/login-template.html',
            controller: 'LoginCtrl as loginCtrl'
          }
        }
      });
  })
  .controller('LoginCtrl', function () {

  });