import React from 'react';
import PropTypes from 'prop-types';
import { pick } from 'lodash';
import TableCell from '../Table/TableCell';
import columnHOC from '../Table/ColumnContainer';
import InputText from './AdvancedTableCell';

const ReduxCell = columnHOC(TableCell);
/**
 * Опертка RowCell
 * @param children - cell
 * @param context
 * @returns {*}
 * @constructor
 */

class AdvancedTableCellRenderer extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      value: props.props.value
    };
  }

  render() {
    const { editing } = this.props;
    const { record, editable, index } = this.props;
    const { redux, propStyles } = this.props.props;
    let component = null;
    if (redux) {
      component = <ReduxCell {...propStyles} {...this.props.props} as={'span'} />;
    } else {
      component = <TableCell {...propStyles} {...this.props.props} as={'span'} />;
    }

    return component;
  }
}

export default AdvancedTableCellRenderer;
