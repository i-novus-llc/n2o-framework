import React from 'react';
import PropTypes from 'prop-types';
import { filter, isString } from 'lodash';
import cn from 'classnames';

function AdvancedTableCell({ rowIndex, columnIndex, children }) {
  const renderCell = () => {
    const components = filter(children, c => c);

    if (components.length > 1 || isString(components[0])) {
      return (
        <td>
          <div className="n2o-advanced-table-cell-expand">{components}</div>
        </td>
      );
    }
    return components;
  };

  return renderCell(children);
}

AdvancedTableCell.propTypes = {
  children: PropTypes.any,
  selectionCell: PropTypes.bool,
  selectionClass: PropTypes.string
};

export default AdvancedTableCell;
