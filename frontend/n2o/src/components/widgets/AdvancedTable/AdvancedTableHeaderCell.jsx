import React, { Component } from 'react';
import PropTypes from 'prop-types';

class AdvancedTableHeaderCell extends Component {
  render() {
    const { children } = this.props;
    console.log('point');
    console.log(this.props);
    if (children && children.props.children) {
      return children.props.children;
    }

    return <th>{children}</th>;
  }
}

AdvancedTableHeaderCell.propTypes = {};

export default AdvancedTableHeaderCell;
