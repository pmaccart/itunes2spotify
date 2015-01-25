
var TrackActionCreators = require('../actions/TrackActionCreators');
var SearchStore = require('../stores/SearchStore');
var TrackSearchComponent = React.createClass({

  getInitialState: function () {
    return {
      results: [],
      search: {
        artist: '',
        track: ''
      }
    };
  },

  componentDidMount: function() {
    SearchStore.addChangeListener(this._onChange);
  },

  componentWillUnmount: function () {
    SearchStore.removeChangeListener(this._onChange);
  },

  render: function () {

    var resultNodes = this.state.results.map(function (result) {
      return (
        <tr key={result.uri}>
          <td>{result.name}</td>
          <td>{result.artist}</td>
          <td>
            <button className="btn btn-link" onClick={this.linkTrack.bind(this, result.uri)}>Select</button>
          </td>
        </tr>
        );
    }.bind(this));

  var search = this.state.search;
    return (
      <div>
        <form name="searchForm" onSubmit={this.doSearch}>
          <div className="form-group">
            <label for="track">Track: </label>
            <input type="text" id="track" className="form-control" placeholder="Track name" value={search.track} onChange={this.onTrackChange}/>
          </div>
          <div className="form-group">
            <label for="artist">Artist: </label>
            <input type="text" id="artist" className="form-control" placeholder="Artist name" value={search.artist} onChange={this.onArtistChange}/>
          </div>
          <button type="submit" className="btn btn-primary">Search</button>
        </form>

        <table className="table table-striped">
          <thead>
            <tr>
              <th>Name</th>
              <th>Arist</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {resultNodes}
          </tbody>
        </table>
      </div>
      );
  },

  linkTrack: function (uri, e) {
    console.log('Linking Track');
    TrackActionCreators.linkTrack(this.props.trackId, uri)
  },

  onTrackChange: function(e) {
    this.setState({
      search: {
        track: e.target.value,
        artist: this.state.search.artist
      }
    });
  },

  onArtistChange: function (e) {
    this.setState({
      search: {
        artist: e.target.value,
        track: this.state.search.track
      }
    });
  },

  doSearch: function() {
    console.log('Performing Search; track=' + this.state.search.track + '; artist=' + this.state.search.artist);
    TrackActionCreators.searchTrack(this.state.search.track, this.state.search.artist);
  },

  _onChange: function() {
    this.setState({
      results: SearchStore.getResults()
    });
  }
});

module.exports = TrackSearchComponent;