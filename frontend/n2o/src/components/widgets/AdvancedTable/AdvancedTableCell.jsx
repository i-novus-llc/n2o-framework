import React from 'react';
import PropTypes from 'prop-types';
import cn from 'classnames';

function AdvancedTableCell({ rowIndex, columnIndex, children, selectionCell, selectionClass }) {
  // let colSpan = 1;
  // let rowSpan = 1;
  // console.log('point')
  // console.log(rowIndex)
  // console.log(columnIndex)
  // if (rowIndex === 0 && columnIndex === 0) {
  //   colSpan = 3;
  // }
  //
  // if (rowIndex === 0 && columnIndex > 0) {
  //   return null;
  // }

  return (
    <td
      // colSpan={colSpan}
      // rowSpan={rowSpan}
      className={cn('n2o-advanced-table-cell', { [selectionClass]: selectionCell })}
    >
      <div className="n2o-advanced-table-cell-content">{children}</div>
    </td>
  );
}

AdvancedTableCell.propTypes = {
  children: PropTypes.any,
  selectionCell: PropTypes.bool,
  selectionClass: PropTypes.string
};

export default AdvancedTableCell;
