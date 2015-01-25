//  var App = React.createClass({
//    componentDidMount: function () {
//      $.ajax({
//        url: '/api/playlists',
//        method: 'GET'
//      })
//        .then(function (response) {
//          this.setState({data: response})
//
//        }.bind(this),
//        function () {
//
//        }.bind(this));
//    },
//    getInitialState: function() {
//      return {data: []};
//    },
//    render: function () {
//      return (
//        <div className="container">
//          <h1>iTunes2Spotify</h1>
//
//          <Playlists data={this.state.data}/>
//        </div>
//        );
//    }
//
//  });




var React = require('react');
//var _ = require('lodash');
//var $ = require('jquery');
//var Backbone = require('backbone');
window.React = React;
var Playlists = require('./components/playlists-component');
var Playlist = require('./components/playlist-component');
var Track = require('./components/track-component');
var WebAPIUtils = require('./utils/WebAPIUtils');


var Router = Backbone.Router.extend({
  routes: {
    ''                                       : 'playlists',
    'playlists/:playlistId'                  : 'playlist',
    'playlists/:playlistId/tracks/:trackId'  : 'track',
    '*path': 'redirect'
  },
  playlists: function() {
    console.log('Rendering playlists component');

    WebAPIUtils.getPlaylists();
    React.render(
      <Playlists />,
      document.getElementById('app')
    )
    return true;
  },
  playlist: function(playlistId) {
    console.log('Rendering playlist component with playlistId=' + playlistId);

    WebAPIUtils.getPlaylist(playlistId);
    React.render(
      <Playlist playlistId={playlistId} />,
      document.getElementById('app')
    )
    return true;
  },
  track: function(playlistId, trackId) {
    console.log('Rendering track component with trackId=' + trackId);

    WebAPIUtils.getTrack(playlistId, trackId);
    React.render(
      <Track playlistId={playlistId} trackId={trackId} />,
      document.getElementById('app')
    )
    return true;
  },
  redirect: function () {
    console.log('Redirecting to the default route');
    window.location.hash = '#/';
  }
});

new Router();
console.log('Router created; starting history.');
Backbone.history.start();
