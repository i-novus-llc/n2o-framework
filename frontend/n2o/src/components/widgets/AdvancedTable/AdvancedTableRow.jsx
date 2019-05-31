import React from 'react';
import { pure } from 'recompose';
import PropTypes from 'prop-types';
import cn from 'classnames';

/**
 * Компонент создания строки в таблице
 * @param props
 * @constructor
 */
function AdvancedTableRow(props) {
  const {
    className,
    isRowActive,
    setRef,
    children,
    model,
    rowClick,
    rowClass,
  } = props;
  const classes = cn(className, 'n2o-table-row n2o-advanced-table-row', {
    'table-active': isRowActive,
    'row-click': !!rowClick,
    [rowClass]: rowClass,
  });
  const newProps = {
    ...props,
    ref: el => setRef && setRef(el, model.id),
    tabIndex: 1,
    key: model.id,
    className: classes,
  };

  return React.createElement('tr', newProps, [...children]);
}

AdvancedTableRow.propTypes = {
  className: PropTypes.string,
  isRowActive: PropTypes.bool,
  setRef: PropTypes.func,
  children: PropTypes.object,
  model: PropTypes.object,
};

export default pure(AdvancedTableRow);
