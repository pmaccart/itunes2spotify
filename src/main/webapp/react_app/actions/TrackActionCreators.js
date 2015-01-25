var AppDispatcher = require('../dispatcher/AppDispatcher');
var I2SConstants = require('../constants/I2SConstants');

var ActionTypes = I2SConstants.ActionTypes;

module.exports = {

  searchTrack: function (track, artist) {
    AppDispatcher.handleViewAction({
      type: ActionTypes.SEARCH_TRACK,
      track: track,
      artist: artist
    });
  },

  linkTrack: function (trackId, uri) {
    AppDispatcher.handleViewAction({
      type: ActionTypes.LINK_TRACK,
      trackId: trackId,
      uri: uri
    });
  }
};