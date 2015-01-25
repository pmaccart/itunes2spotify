/** @jsx React.DOM */

var PlaylistStore = require('../stores/PlaylistStore');
var TrackSearch = require('./track-search-component');
var Track = React.createClass({
  getDefaultProps: function () {
    return {
      playlistId: null,
      trackId: null
    };
  },
  getInitialState: function () {
    return {
      track: {},
      showSearch: false
    };
  },

  componentDidMount: function () {
    PlaylistStore.addChangeListener(this._onChange);
  },
  componentWillUnmount: function () {
    PlaylistStore.removeChangeListener(this._onChange);
  },
  render: function () {
    console.log('Rendering track component; showSearch=' + this.state.showSearch);
    var track = this.state.track;
    var backHref = "#playlists/" + this.props.playlistId;

    return (
      <div>
        <div className="panel panel-primary">
          <div className="panel-heading">
            <h3 className="panel-title">
              <span>{track.name} - {track.artist}</span>
              <span className="pull-right"><a className="btn btn-default" href={backHref}>Back</a></span>
            </h3>
          </div>
          <div className="panel-body">
            <div className="row">
              <div className="col-xs-4">Name:</div>
              <div className="col-xs-8">{track.name}</div>
            </div>
            <div className="row">
              <div className="col-xs-4">Artist:</div>
              <div className="col-xs-8">{track.artist}</div>
            </div>
            <div className="row">
              <div className="col-xs-4">Album:</div>
              <div className="col-xs-8">{track.album}</div>
            </div>
            <div className="row">
              <div className="col-xs-4">Year:</div>
              <div className="col-xs-8">{track.year}</div>
            </div>
            <div className="row">
              <div className="col-xs-4">Spotify URI:</div>
              <div className="col-xs-8">{track.spotifyUri} |
                <button onClick={this.showSearch} className="btn btn-link">Edit</button>
              </div>
            </div>


            {this.state.showSearch ? <TrackSearch trackId={track.trackId} /> : null}

          </div>
        </div>
      </div>
      );
  },

  showSearch: function () {
    console.log('Showing Search');
    this.setState({
      showSearch: true
    });

  },

  _onChange: function () {
    console.log('On change event handler executing in track-component.js');
    this.setState({
      track: PlaylistStore.getTrack(this.props.playlistId, this.props.trackId)
    });
  }


});

module.exports = Track;