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
    this.renderCellContent = this.renderCellContent.bind(this);
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

  renderCellContent() {
    const { redux, cellOptions, row } = this.props;
    const propStyles = pick(this.props, ['width']);
    return redux ? (
      <ReduxCell {...propStyles} {...cellOptions} {...this.props} as={'span'} model={row} />
    ) : (
      <TableCell {...propStyles} {...cellOptions} {...this.props} as={'span'} model={row} />
    );
  }

  render() {
    const { editable, edit } = this.props;

    const { editing } = this.state;
    return (
      <div
        className={cn({ 'n2o-advanced-table-editable-cell': editable })}
        onClick={event => this.toggleEdit(event)}
      >
        {this.renderCellContent()}
        {editable && editing && this.createEditControl(edit)}
      </div>
    );
  }
}

export default AdvancedTableCellRenderer;
