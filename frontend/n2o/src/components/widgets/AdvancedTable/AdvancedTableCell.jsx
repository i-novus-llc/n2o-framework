import React from 'react';
import PropTypes from 'prop-types';
import cn from 'classnames';

function AdvancedTableCell({ rowIndex, columnIndex, children, selectionCell, selectionClass }) {
  console.log('point');
  console.log(children);
  return children;
}

AdvancedTableCell.propTypes = {
  children: PropTypes.any,
  selectionCell: PropTypes.bool,
  selectionClass: PropTypes.string
};

export default AdvancedTableCell;
