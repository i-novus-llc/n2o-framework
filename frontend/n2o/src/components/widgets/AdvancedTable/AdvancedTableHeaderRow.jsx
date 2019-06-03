import React from 'react';

/**
 * Компонент создания строки заголовка
 * @param props
 * @constructor
 */
function AdvancedTableHeaderRow(props) {
  return React.createElement(
    'tr',
    {
      ...props,
      className: 'n2o-advanced-table-header-row',
    },
    [...props.children]
  );
}

export default AdvancedTableHeaderRow;
