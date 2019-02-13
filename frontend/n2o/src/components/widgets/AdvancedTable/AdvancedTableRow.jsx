import React from 'react';
import cn from 'classnames';

function AdvancedTableRow(props) {
  const { color, className, isRowActive, setRef, children, model } = props;
  const classes = cn(className, 'n2o-table-row n2o-advanced-table-row', {
    [`table-${color}`]: color,
    'table-active': isRowActive
  });
  return React.createElement(
    'tr',
    {
      ...props,
      ref: el => setRef && setRef(el, model.id),
      tabIndex: 1,
      key: model.id,
      className: classes
    },
    [...children]
  );
}

AdvancedTableRow.propTypes = {};

export default AdvancedTableRow;
