angular.module('i2s', [
  'ngCookies',
  'ngSanitize',
  'ui.router',
  'ui.bootstrap',
  'i2s.home',
  'i2s.playlist',
  'i2s.track'
])
  .config(function ($urlRouterProvider, $stateProvider) {
    $urlRouterProvider.otherwise('/playlists');
    $stateProvider
      .state('playlists', {
        url: '/playlists',
//        views: {
//          main: {
            controller: 'HomeCtrl as homeCtrl',
            templateUrl: 'app/pages/home/home-template.html'
//          }
//        }
      })
      .state('playlist', {
        url: '/playlists/:playlistId',
//        views: {
//          main: {
            controller: 'PlaylistCtrl as playlistCtrl',
            templateUrl: 'app/pages/playlist/playlist-template.html'
//          }
//        }
      })
      .state('track', {
        url: '/playlists/:playlistId/tracks/:trackId',
//        views: {
//          main: {
            controller: 'TrackCtrl as trackCtrl',
            templateUrl: 'app/pages/track/track-template.html'
//          }
//        }
      });
  })
  .controller('AppCtrl', function ($scope) {

  });