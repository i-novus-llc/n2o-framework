import React from 'react';

function AdvancedTableHeaderRow(props) {
  return React.createElement(
    'tr',
    {
      ...props,
      className: 'n2o-advanced-table-header-row'
    },
    [...props.children]
  );
}

AdvancedTableHeaderRow.propTypes = {};

export default AdvancedTableHeaderRow;
