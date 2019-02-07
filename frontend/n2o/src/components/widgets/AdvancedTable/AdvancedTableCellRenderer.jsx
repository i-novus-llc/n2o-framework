import React from 'react';
import PropTypes from 'prop-types';
import { pick } from 'lodash';
import TableCell from '../Table/TableCell';
import columnHOC from '../Table/ColumnContainer';

const ReduxCell = columnHOC(TableCell);
/**
 * Опертка RowCell
 * @param children - cell
 * @param context
 * @returns {*}
 * @constructor
 */

function AdvancedTableCellRenderer({ props, record }) {
  const { redux, propStyles } = props;
  const { index } = props;
  let component = null;
  if (redux) {
    component = <ReduxCell {...propStyles} {...props} as={'td'} />;
  } else {
    component = <TableCell {...propStyles} {...props} as={'td'} />;
  }
  return component;
}

export default AdvancedTableCellRenderer;
