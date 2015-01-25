var ApiServerActionCreators = require('../actions/ApiServerActionCreators');

module.exports = {
  getPlaylists: function() {
    $.ajax({
      url: '/api/playlists',
      method: 'GET'
    }).then(function(response) {
      ApiServerActionCreators.receiveAll(response);
    });
  },

  getPlaylist: function(id) {
    $.ajax({
      url: '/api/playlists/' + id,
      method: 'GET'
    }).then(function (response) {
      ApiServerActionCreators.receiveOne(response);
    });
  },

  getTrack: function(playlistId, trackId) {
    ApiServerActionCreators.receiveTrack(playlistId, trackId);
  }
};