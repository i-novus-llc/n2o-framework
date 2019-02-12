import React, { Component } from 'react';
import cn from 'classnames';
import { omit } from 'lodash';
import PropTypes from 'prop-types';

class AdvancedTableRow extends Component {
  render() {
    const { color, className, isRowActive, setRef, index, children } = this.props;
    const classes = cn(className, 'n2o-table-row n2o-advanced-table-row', {
      [`table-${color}`]: color,
      'table-active': isRowActive
    });
    console.log('point');
    console.log(this.props);
    return React.createElement(
      'tr',
      {
        ...this.props,
        ref: el => setRef && setRef(el, index),
        tabIndex: 1,
        key: index,
        className: classes
      },
      [...children]
    );
  }
}

AdvancedTableRow.propTypes = {};

export default AdvancedTableRow;
