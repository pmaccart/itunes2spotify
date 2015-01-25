var AppDispatcher = require('../dispatcher/AppDispatcher');
var EventEmitter = require('events').EventEmitter;
var I2SConstants = require('../constants/I2SConstants');
var $ = require('jquery');
var assign = require('object-assign');


var ActionTypes = I2SConstants.ActionTypes;
var CHANGE_EVENT = 'change';

var _searchResults = [];
var SearchStore = assign({}, EventEmitter.prototype, {

  addChangeListener: function(listener) {
    this.on(CHANGE_EVENT, listener)
  },

  removeChangeListener: function(listener) {
    this.removeListener(CHANGE_EVENT, listener);
  },

  emitChange: function() {
    this.emit(CHANGE_EVENT);
  },

  search: function(track, artist) {
    $.ajax({
      url: '/api/spotify/search',
      type: 'GET',
      data: {
        track: track,
        artist: artist
      }
    }).then(function (response) {
      if (!(response && response.tracks && response.tracks.items && response.tracks.items.length)) {
        _searchResults = [];
      }
      else {
        _searchResults = response.tracks.items.map(function (item) {
          return {
            name: item.name,
            artist: item.artists[0].name,
            album: item.album.name,
            uri: item.uri
          };
        });
      }
      this.emitChange();
    }.bind(this));
  },

  getResults: function() {
    return _searchResults;
  }

});

AppDispatcher.register(function (payload) {
  var action = payload.action;
  switch (action.type) {
    case ActionTypes.SEARCH_TRACK:
      SearchStore.search(action.track, action.artist);
      break;
    default:
      // do nothing

  }
});

module.exports = SearchStore;