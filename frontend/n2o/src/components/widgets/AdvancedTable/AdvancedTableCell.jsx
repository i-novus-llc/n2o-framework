import React, { Component } from 'react';
import { map, get } from 'lodash';
import columnHOC from '../Table/ColumnContainer';
import TableCell from '../Table/TableCell';

const ReduxCell = columnHOC(TableCell);

class AdvancedTableCell extends Component {
  constructor(props) {
    super(props);

    this.state = {
      editing: false,
      value: this.getCellValue()
    };

    this.toggleEdit = this.toggleEdit.bind(this);
    this.handleClickOutside = this.handleClickOutside.bind(this);
    this.onChange = this.onChange.bind(this);
    this.setInputRef = this.setInputRef.bind(this);
    this.getCellValue = this.getCellValue.bind(this);
  }

  setInputRef(ref) {
    this.input = ref;
  }

  onChange(value) {
    this.setState({ value });
  }

  toggleEdit() {
    const editing = !this.state.editing;
    this.setState({ editing });
  }

  handleClickOutside(e) {
    const { editing } = this.state;
  }

  getCellValue() {
    const { children } = this.props;
    return map(children, item => {
      const value = get(item, 'props.value');
      if (item && value) return value;
    }).join('');
  }

  render() {
    const { editing } = this.state;
    const { editable, record, children, index, src, redux, width } = this.props;
    const cellContent = redux ? <TableCell /> : <ReduxCell />;

    return (
      <td className="n2o-advanced-table-cell">
        <div className="n2o-advanced-table-cell-content">{children}</div>
      </td>
    );
  }
}

AdvancedTableCell.propTypes = {};

export default AdvancedTableCell;
