import React from 'react';
import cn from 'classnames';
import PropTypes from 'prop-types';
import { pick } from 'lodash';
import TableCell from '../Table/TableCell';
import Factory from '../../../core/factory/Factory';
import columnHOC from '../Table/ColumnContainer';

const ReduxCell = columnHOC(TableCell);
/**
 * Опертка RowCell
 * @param children - cell
 * @param context
 * @returns {*}
 * @constructor
 */

class AdvancedTableCellRenderer extends React.Component {
  render() {
    const { component } = this.props;
    return component;
  }
}

export default AdvancedTableCellRenderer;
