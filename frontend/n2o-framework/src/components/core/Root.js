import React from 'react';
import PropTypes from 'prop-types';
import { pure } from 'recompose';
import OverlayPages from './OverlayPages';
import GlobalAlerts from './GlobalAlerts';

function Root({ children }) {
  return (
    <React.Fragment>
      <GlobalAlerts />
      {children}
      <OverlayPages />
    </React.Fragment>
  );
}

Root.propTypes = {
  children: PropTypes.node,
};

export default pure(Root);
