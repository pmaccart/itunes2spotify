angular.module('i2s.home', [

])
  .factory('playlistService', function ($http, $q) {
    return {
      getPlaylists: function () {
        var deferred = $q.defer();
        $http({
          method: 'GET',
          url: '/api/playlists',
          cache: true
        }).then(function (response) {
          deferred.resolve(response.data);
        }, function (response) {
          deferred.reject(response);
        });

        return deferred.promise;
      },

      getPlaylist: function (playlistId) {
        var deferred = $q.defer();
        $http({
          method: 'GET',
          url: '/api/playlists/' + playlistId,
          cache: true
        }).then(function (response) {
          deferred.resolve(response.data);
        }, function (response) {
          deferred.reject(response);
        });

        return deferred.promise;
      }
    }
  })
  .controller('HomeCtrl', function ($log, $scope, $http, playlistService) {
    $scope.playlists = [];
    playlistService.getPlaylists()
      .then(function (playlists) {
        $scope.playlists = playlists;
      }, function (error) {
        $log.error('Error retrieving playlists.', error);
        $scope.playlists = null;
      });

  });