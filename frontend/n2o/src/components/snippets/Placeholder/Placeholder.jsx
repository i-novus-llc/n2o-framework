import React, { createElement } from 'react';
import PropTypes from 'prop-types';
import components from './components';

function Placeholder({ loading, type, children, ...rest }) {
  return loading ? createElement(components[type], rest) : children || null;
}

Placeholder.propTypes = {
  loading: PropTypes.bool,
  type: PropTypes.string,
  children: PropTypes.node,
};

Placeholder.defaultProps = {
  loading: false,
  type: 'list',
};

export default Placeholder;
