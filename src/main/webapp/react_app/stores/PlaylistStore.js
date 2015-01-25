
var AppDispatcher = require('../dispatcher/AppDispatcher');
var EventEmitter = require('events').EventEmitter;
var I2SConstants = require('../constants/I2SConstants');
var assign = require('object-assign');
var $ = require('jquery');

var ActionTypes = I2SConstants.ActionTypes;
var CHANGE_EVENT = 'change';



var _allPlaylists = {};
var _playlists = {};

var PlaylistStore = assign({}, EventEmitter.prototype, {
  init: function (rawPlaylists) {
    rawPlaylists.forEach(function (rawPlaylist) {
       var playlistId = rawPlaylist.playlistId;
      _allPlaylists[playlistId] = rawPlaylist;
    });
  },

  emitChange: function() {
    this.emit(CHANGE_EVENT);
  },

  addChangeListener: function(listener) {
    this.on(CHANGE_EVENT, listener)
  },

  removeChangeListener: function(listener) {
    this.removeListener(CHANGE_EVENT, listener); 
  },

  get:function(id) {
    return _playlists[id];
  },

  getAll: function() {
    return _allPlaylists;
  },

  getTrack: function(playlistId, trackId) {
    return _.find(_playlists[playlistId].tracks, function(track) {
      return track.trackId === trackId;
    });
  },

  _linkTrack: function (trackId, uri) {
    $.ajax({
      url: '/api/tracks/' + trackId + '/spotifyUri/' + uri,
      type: 'PUT'
    }).then(function (response) {
      this.emitChange();
    }.bind(this))
  },


  set: function (id, rawPlaylist) {
    _playlists[id] = rawPlaylist;
  },

  _fetchPlaylists: function() {
    if (_allPlaylists && _allPlaylists.length) {
      this.emit(CHANGE_EVENT);
    }
    else {
      $.ajax({
        url: '/api/playlists',
        type: 'GET'
      }).then(function (response) {
        _allPlaylists = response;
        this.emit(CHANGE_EVENT);
      }.bind(this))
    }
  },

  _fetchPlaylist: function(playlistId) {
    if (_playlists[playlistId] && _playlists[playlistId].tracks[trackId]) {
      this.emit(CHANGE_EVENT);
    }
    else {
      $.ajax({
        url: '/api/playlists/' + playlistId,
        type: 'GET'
      }).then(function (response) {
        _playlists[response.playlistId] = response;
        this.emit(CHANGE_EVENT);
      }.bind(this))
    }
  },

  _fetchTrack: function(playlistId, trackId) {
    console.log('Fetching track.');
    if (_playlists[playlistId] && _playlists[playlistId].tracks[trackId]) {
      console.log('Playlist was already loaded; no need to load from server.');
      this.emit(CHANGE_EVENT);
    }
    else {
      console.log('Loading playlist and track from server.');
      $.ajax({
        url: '/api/playlists/' + playlistId,
        type: 'GET'
      }).then(function (response) {
        _playlists[response.playlistId] = response;
        this.emit(CHANGE_EVENT);
      }.bind(this))
    }
  }
});

PlaylistStore.dispatchToken = AppDispatcher.register(function(payload) {
  var action = payload.action;

  switch (action.type) {
    case ActionTypes.FETCH_PLAYLISTS:
      PlaylistStore.init(action.rawPlaylists);
      PlaylistStore.emitChange();
      break;
    case ActionTypes.FETCH_PLAYLIST:
      PlaylistStore.set(action.rawPlaylist.playlistId, action.rawPlaylist);
      PlaylistStore.emitChange();
      break;

    case ActionTypes.FETCH_TRACK:
      PlaylistStore._fetchTrack(action.playlistId, action.trackId);
      break;
    case ActionTypes.LINK_TRACK:
      PlaylistStore._linkTrack(action.trackId, action.uri);
      break;

    default:
      console.log('Unknown action.', action);
  }
});

module.exports = PlaylistStore;

