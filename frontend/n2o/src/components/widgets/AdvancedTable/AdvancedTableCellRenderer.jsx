import React from 'react';

/**
 * Опертка RowCell
 * @param children - cell
 * @returns {*}
 * @constructor
 */
function AdvancedTableCellRenderer({ children }) {
  return children ? children : null;
}

export default AdvancedTableCellRenderer;
