import PropTypes from "prop-types";

import createFetchProxy from "react-cosmos-fetch-proxy";
import createReduxProxy from "react-cosmos-redux-proxy";
import createRouterProxy from "react-cosmos-router-proxy";
import createXhrProxy from "react-cosmos-xhr-proxy";
import createContextProxy from "react-cosmos-context-proxy";

// We can import app files here
import history from "n2o-framework/lib/history";
import configureStore from "n2o-framework/lib/store";

// Read more about configuring Redux in the Redux proxy section below
const ReduxProxy = createReduxProxy({
  createStore: state =>
    configureStore(state, history, {
      security: {},
      messages: {}
    })
});

const ContextProxy = createContextProxy({
  childContextTypes: {
    getFromConfig: PropTypes.func,
    getComponent: PropTypes.func
  }
});

// We ensure a specific proxy order
export default [
  // Not all proxies have options, and often relying on defaults is good enough
  createFetchProxy(),
  createXhrProxy(),
  ReduxProxy,
  ContextProxy,
  createRouterProxy()
];
