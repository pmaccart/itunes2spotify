angular.module('i2s.track', [])
.controller('TrackCtrl', function($scope, $stateParams, $http) {
  $http({
    method: 'GET',
    url: '/api/tracks/' + $stateParams.trackId
  }).then(function (response) {
    $scope.track = response.data;
  }, function () {
    $scope.errorMsg = 'Error loading track.';
  });

  this.doSearch = function (track, artist) {
    $scope.searchErrorMsg = null;

    $http({
      method: 'GET',
      url: '/api/spotify/search',
      params: {
        track: track,
        artist: artist
      }
    }).then(function (response) {
      if (!(response && response.data && response.data.tracks 
        && response.data.tracks.items && response.data.tracks.items.length)) {
        $scope.searchErrorMsg = "No results found.";
      }
      else {
        var items = response.data.tracks.items;
        $scope.searchResults = items.map(function (item) {
          return {
            name: item.name,
            artist: item.artists[0].name,
            album: item.album.name,
            uri: item.uri
          }
        });
      }

    }, function () {
      $scope.searchErrorMsg = "Error performing search.";
    });
  };

  this.link = function(uri) {
    $http({
      method: 'PUT',
      url: '/api/tracks/' + $stateParams.trackId + '/spotifyUri/' + uri
    }).then(function (response) {
      $scope.linkMessage = "Track updated!";
      $scope.track = response.data;
    });
  }


});