
var I2SConstants = require('../constants/I2SConstants');
var Dispatcher = require('flux').Dispatcher;
var assign = require('object-assign');

var PayloadSources = I2SConstants.PayloadSources;

var AppDispatcher = assign(new Dispatcher(), {
  handleServerAction: function (action) {
    return this._handleAction(PayloadSources.SERVER_ACTION, action);
  },

  handleViewAction: function (action) {
    return this._handleAction(PayloadSources.VIEW_ACTION, action);
  },

  _handleAction: function (source, action) {
    this.dispatch({
      source: source,
      action: action
    });
  }
});

module.exports = AppDispatcher;