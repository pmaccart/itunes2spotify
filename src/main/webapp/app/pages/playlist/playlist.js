angular.module('i2s.playlist', [])
  .controller('PlaylistCtrl', function ($scope, $timeout, $stateParams, $state, $http, playlistService) {
    var playlistId = $stateParams.playlistId;
    if (!playlistId) {
      $state.go('playlists');
    }
    else {
      playlistService.getPlaylist(playlistId)
        .then(function (playlist) {
          $scope.playlist = playlist;
        }, function (error) {
          $scope.errorMsg = 'Error retreiving playlist.';
        });
    }

    this.linkPlaylist = function (playlist) {
      $http({
        method: 'POST',
        url: '/api/playlists/' + playlist.playlistId + '/link'
      }).then(function (response) {
        $scope.playlists.forEach(function (p, index) {
          if (p.playlistId === playlist.playlistId) {
            $scope.playlists[index] = playlist;
          }
        });
      });
    };

    this.publishPlaylist = function (playlist) {
      $scope.publishing = true;
      $http({
        method: 'POST',
        url: '/api/playlists/' + playlist.playlistId + '/publish'
      }).then(function (response) {
        $scope.alertClass = "alert-success";
        $scope.alertMessage = "Playlist successfully published!";
      }, function () {
        $scope.alertClass = "alert-danger";
        $scope.alertMessage = "There was an error publishing the playlist.";
      }).finally(function () {
        $scope.publishing = false;
        $scope.showAlert = true;
        $timeout(function () {
          $scope.showAlert = false;
        }, 5000);
      })

    }

    $scope.predicate = '-playCount';
  });