import React, { Component } from 'react';
import cn from 'classnames';
import PropTypes from 'prop-types';

class List extends Component {
  render() {
    const { className } = this.props;
    return <div className={cn('n2o-widget-list', className)} />;
  }
}

List.propTypes = {};
List.defaultProps = {};

export default List;
