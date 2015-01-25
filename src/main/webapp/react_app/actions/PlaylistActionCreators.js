var AppDispatcher = require('../dispatcher/AppDispatcher');
var I2SConstants = require('../constants/I2SConstants');

var ActionTypes = I2SConstants.ActionTypes;

module.exports = {

  clickPlaylist: function (playlistId) {
    AppDispatcher.handleViewAction({
      type: ActionTypes.CLICK_PLAYLIST,
      playlistId: playlistId
    });
  },

  clickTrack: function (trackId) {
    AppDispatcher.handleViewAction({
      type: ActionTypes.CLICK_TRACK,
      trackId: trackId
    });
  }
};