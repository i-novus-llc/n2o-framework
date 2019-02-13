import React from 'react';
import PropTypes from 'prop-types';
import cn from 'classnames';

function AdvancedTableCell({ children, selectionCell, selectionClass, colSpan, rowSpan }) {
  return (
    <td className={cn('n2o-advanced-table-cell', { [selectionClass]: selectionCell })}>
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
