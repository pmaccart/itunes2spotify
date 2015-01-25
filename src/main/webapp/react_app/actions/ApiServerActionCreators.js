var AppDispatcher = require('../dispatcher/AppDispatcher');
var I2SConstants = require('../constants/I2SConstants');

var ActionTypes = I2SConstants.ActionTypes;

module.exports = {

  receiveAll: function (rawPlaylists) {
    AppDispatcher.handleServerAction({
      type: ActionTypes.FETCH_PLAYLISTS,
      rawPlaylists: rawPlaylists
    });
  },

  receiveOne: function (rawPlaylist) {
    AppDispatcher.handleServerAction({
      type: ActionTypes.FETCH_PLAYLIST,
      rawPlaylist: rawPlaylist
    });
  },

  receiveTrack: function (playlistId, trackId) {
    AppDispatcher.handleServerAction({
      type: ActionTypes.FETCH_TRACK,
      playlistId: playlistId,
      trackId: trackId
    })
  }
};