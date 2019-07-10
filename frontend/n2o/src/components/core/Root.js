import React from 'react';
import PropTypes from 'prop-types';
import { pure } from 'recompose';
import ModalPages from './ModalPages';
import GlobalAlerts from './GlobalAlerts';

function Root({ children }) {
  return (
    <React.Fragment>
      <GlobalAlerts />
      {children}
      <ModalPages />
    </React.Fragment>
  );
}

Root.propTypes = {
  children: PropTypes.node,
};

export default pure(Root);
