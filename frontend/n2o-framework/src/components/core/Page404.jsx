import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { getContext } from 'recompose';

class Page404 extends Component {
  render() {
    return <h1>{this.props.t('pageNotFound')}</h1>;
  }
}

Page404.propTypes = {};
Page404.defaultProps = {};

export default getContext({ t: PropTypes.func })(Page404);
