import React, { Component } from 'react';
import cn from 'classnames';
import { omit } from 'lodash';
import PropTypes from 'prop-types';

class AdvancedTableRow extends Component {
  render() {
    const { color, className, isRowActive, setRef, index } = this.props;
    const classes = cn(className, 'n2o-table-row n2o-advanced-table-row', {
      [`table-${color}`]: color,
      'table-active': isRowActive
    });
    return React.createElement(
      'tr',
      {
        ...this.props,
        ref: el => setRef && setRef(el, index),
        tabIndex: 1,
        className: classes
      },
      this.props.children
    );
  }
}

AdvancedTableRow.propTypes = {};

export default AdvancedTableRow;
