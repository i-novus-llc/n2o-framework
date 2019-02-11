import React from 'react';
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
  constructor(props) {
    super(props);

    this.state = {
      value: props.value,
      editing: false
    };

    this.toggleEdit = this.toggleEdit.bind(this);
    this.onChange = this.onChange.bind(this);
  }

  toggleEdit() {
    this.setState({ editing: !this.state.editing });
  }

  onChange(value) {
    this.setState({ value });
  }

  render() {
    const { component, redux, value, row, cell, editable, edit } = this.props;
    const { editing } = this.state;

    const cellContent = redux ? (
      <ReduxCell {...this.props} {...cell} label={value} as={'span'} model={row} />
    ) : (
      <TableCell {...this.props} {...cell} label={value} as={'span'} model={row} />
    );

    return (
      <div onDoubleClick={this.toggleEdit}>
        {cellContent}
        {editable &&
          editing &&
          React.createElement(edit.component, {
            autoFocus: true,
            value: this.state.value,
            onChange: this.onChange,
            onBlur: this.toggleEdit
          })}
      </div>
    );
  }
}

export default AdvancedTableCellRenderer;
