/** @jsx React.DOM */
var PlaylistStore = require('../stores/PlaylistStore');

var Playlist = React.createClass({
  getInitialState: function() {
    return {
      playlist: {
        name: null,
        tracks: []
      }
    };
  },
  componentDidMount: function() {
    // $.ajax({
    //   url: '/api/playlists/' + this.props.playlistId,
    //   method: 'GET'
    // }).then(function (response) {
    //   this.setState({
    //     playlist: response
    //   })
    // }.bind(this));

    PlaylistStore.addChangeListener(this._onChange);
  },

  componentWillUnmount: function() {
    PlaylistStore.removeChangeListener(this._onChange);
  },
  handleTrackClick: function (track) {
    // React.render(
    //   <Track track={track} />,
    //   this.getDOMNode()
    // );
    // return false;
  },
  render: function() {
    var playlistId = this.props.playlistId;
    var trackNodes = this.state.playlist.tracks.map(function (track) {
      var trackHref = '#playlists/' + playlistId + '/tracks/' + track.trackId;

      return (
        <tr key={track.trackId}>
          <td><a href={trackHref} onClick={this.handleTrackClick.bind(this, track)}>{track.name}</a></td>
          <td>{track.artist}</td>
          <td>{track.album}</td>
          <td>{track.genre}</td>
          <td>{track.year}</td>
          <td>{track.playCount}</td>
          <td>{track.spotifyUri}</td>
        </tr>
      );
    }.bind(this));

    return (
      <div>
        <h4>
          <span>{this.state.playlist.name}</span>
          <button className="btn btn-default" ng-click="playlistCtrl.linkPlaylist(playlist)">Link Playlist</button>
          <span className="glyphicon glyphicon-ok" ng-show="playlist.spotifyId != null"></span>
          <button className="btn btn-primary" ng-click="playlistCtrl.publishPlaylist(playlist)">Publish</button>
        </h4>
        <div className="alert alert-info" ng-show="publishing">Publishing...</div>
        <div className="alert" ng-show="sholwAlert" ng-class="alertClass" ng-bind="alertMessage"></div>
        <div>
          <table className="table table-striped">
            <thead>
              <tr>
                <th ng-click="predicate = (predicate == 'name' ? '-name' : 'name')">Name</th>
                <th ng-click="predicate = (predicate == 'artist' ? '-artist' : 'artist')">Artist</th>
                <th ng-click="predicate = (predicate == 'album' ? '-album' : 'album')">Album</th>
                <th ng-click="predicate = (predicate == 'genre' ? '-genre' : 'genre')">Genre</th>
                <th ng-click="predicate = (predicate == 'year' ? '-year' : 'year')">Year</th>
                <th ng-click="predicate = (predicate == 'playCount' ? '-playCount' : 'playCount')">Playcount</th>
                <th ng-click="predicate = (predicate == 'spotifyUri playCount' ? '-spotifyUri playCount' : 'spotifyUri')">Linked</th>
              </tr>
            </thead>
            <tbody>
              {trackNodes}
            </tbody>
          </table>
        </div>
      </div>
    );
  },

  publishPlaylist: function () {

  },

  linkPlaylist: function () {

  },

  _onChange: function() {
    this.setState({
      playlist: PlaylistStore.get(this.props.playlistId)
    });
  }
});

module.exports = Playlist;