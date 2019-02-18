import React from 'react';
import PropTypes from 'prop-types';
import { filter, isString } from 'lodash';
import cn from 'classnames';

function AdvancedTableCell({ rowIndex, columnIndex, children, hasSpan, record = {} }) {
  const { span } = record;
  let colSpan = 1;
  let rowSpan = 1;

  if (hasSpan && span) {
    if (span.colSpan === 0 || span.rowSpan === 0) {
      return null;
    }
    colSpan = span.colSpan;
    rowSpan = span.rowSpan;
  }

  return (
    <td colSpan={colSpan} rowSpan={rowSpan}>
      <div className="n2o-advanced-table-cell-expand">{children}</div>
    </td>
  );
}

AdvancedTableCell.propTypes = {
  children: PropTypes.any,
  selectionCell: PropTypes.bool,
  selectionClass: PropTypes.string
};

export default AdvancedTableCell;
