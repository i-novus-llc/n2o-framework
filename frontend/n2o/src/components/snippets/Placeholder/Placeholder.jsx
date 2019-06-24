import React, { createElement, Component } from 'react';
import PropTypes from 'prop-types';
import components from './components';

class Placeholder extends Component {
  constructor(props) {
    super(props);
    this.stopRender = false;
  }

  componentDidUpdate(prevProps) {
    if (prevProps.loading && !this.props.loading && !this.stopRender) {
      this.stopRender = true;
    }
  }

  render() {
    const { loading, type, children, once, ...rest } = this.props;

    if (!once && loading) {
      return createElement(components[type], rest);
    }
    if (once && !this.stopRender && loading) {
      return createElement(components[type], rest);
    }

    return children || null;
  }
}

Placeholder.propTypes = {
  loading: PropTypes.bool,
  type: PropTypes.string,
  children: PropTypes.node,
  once: PropTypes.bool,
};

Placeholder.defaultProps = {
  loading: false,
  type: 'list',
  once: false,
};

export default Placeholder;
