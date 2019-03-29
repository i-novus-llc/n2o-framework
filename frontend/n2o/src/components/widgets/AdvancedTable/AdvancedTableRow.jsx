import React from 'react';
import PropTypes from 'prop-types';
import cn from 'classnames';

/**
 * Компонент создания строки в таблице
 * @param props
 * @constructor
 */
function AdvancedTableRow(props) {
  const {
    color,
    className,
    isRowActive,
    setRef,
    children,
    model,
    rowClick,
  } = props;
  const classes = cn(className, 'n2o-table-row n2o-advanced-table-row', {
    [`table-${color}`]: color,
    'table-active': isRowActive,
    'row-click': !!rowClick,
  });
  return React.createElement(
    'tr',
    {
      ...props,
      ref: el => setRef && setRef(el, model.id),
      tabIndex: 1,
      key: model.id,
      className: classes,
    },
    [...children]
  );
}

AdvancedTableRow.propTypes = {
  color: PropTypes.string,
  className: PropTypes.string,
  isRowActive: PropTypes.bool,
  setRef: PropTypes.func,
  children: PropTypes.object,
  model: PropTypes.object,
};

export default AdvancedTableRow;
