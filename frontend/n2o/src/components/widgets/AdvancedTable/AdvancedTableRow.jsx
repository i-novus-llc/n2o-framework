import React, { Component } from 'react';
import cn from 'classnames';
import PropTypes from 'prop-types';

class AdvancedTableRow extends Component {
  render() {
    const {
      id,
      children,
      color,
      className,
      onClick,
      isRowActive,
      onFocus,
      setRef,
      index
    } = this.props;

    return (
      <tr
        className={cn(className, 'n2o-table-row n2o-advanced-table-row', {
          [`table-${color}`]: color,
          'table-active': isRowActive
        })}
        onClick={onClick}
        onFocus={onFocus}
        ref={el => setRef && setRef(el, index)}
        tabIndex={1}
      >
        {children}
      </tr>
    );
  }
}

AdvancedTableRow.propTypes = {};

export default AdvancedTableRow;
