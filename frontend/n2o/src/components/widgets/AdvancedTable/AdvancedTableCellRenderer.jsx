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
  constructor(props) {
    super(props);

    this.state = {
      value: props.value,
      editing: false
    };

    this.onChange = this.onChange.bind(this);
    this.createEditControl = this.createEditControl.bind(this);
  }

  toggleEdit(event, changeValue = false) {
    event.stopPropagation();
    const { onEdit, index, id } = this.props;
    this.setState({ editing: !this.state.editing });
    if (changeValue) {
      onEdit(this.state.value, index, id);
    }
  }

  onChange(value) {
    this.setState({ value });
  }

  createEditControl(edit) {
    return (
      <div className={'n2o-advanced-table-edit-control'}>
        {React.createElement(edit, {
          className: 'n2o-advanced-table-edit-control',
          autoFocus: true,
          value: this.state.value,
          onChange: this.onChange,
          onBlur: event => this.toggleEdit(event, true)
        })}
      </div>
    );
  }

  render() {
    const { editable, edit, component } = this.props;
    const { editing } = this.state;
    return component;
  }
}

export default AdvancedTableCellRenderer;
