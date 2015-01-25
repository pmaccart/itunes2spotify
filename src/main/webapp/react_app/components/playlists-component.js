/** @jsx React.DOM */
var PlaylistStore = require('../stores/PlaylistStore');

var Playlists = React.createClass({

  getInitialState: function() {
    console.log('Returning initial state.');
    return {
      playlists: []
    };
  },
  componentDidMount: function() {

    PlaylistStore.addChangeListener(this._onChange);
  },

  componentWillUnmount: function () {
    PlaylistStore.removeChangeListener(this._onChange);
  },

  render: function () {
    console.log('Rendering: ')
    var playlistNodes = _.map(this.state.playlists, function (playlist, playlistId) {
      var href = '#/playlists/' + playlistId;
      return (
        <div key={playlistId}>
          <h4>
            <a href={href}>{playlist.name}</a>
          </h4>
        </div>
      );
    });

    return (
      <div>
        <h1>iTunes2Spotify</h1>
        <h2>Your Playlists</h2>

        {playlistNodes}

      </div>
    );
  },

  _onChange: function() {
    this.setState({
      playlists: PlaylistStore.getAll()
    });
  }
});

module.exports = Playlists;