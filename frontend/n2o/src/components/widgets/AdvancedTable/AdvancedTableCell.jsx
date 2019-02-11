import React, { Component } from 'react';
import { map, get } from 'lodash';
import InputText from '../../controls/InputText/InputText';
import PropTypes from 'prop-types';

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

  // componentDidMount() {
  //   if (this.props.editable) {
  //     document.addEventListener('click', this.handleClickOutside, true);
  //   }
  // }
  //
  // componentWillUnmount() {
  //   if (this.props.editable) {
  //     document.removeEventListener('click', this.handleClickOutside, true);
  //   }
  // }

  setInputRef(ref) {
    this.input = ref;
  }

  onChange(value) {
    this.setState({ value });
  }

  toggleEdit(event) {
    const editing = !this.state.editing;
    this.setState({ editing }, () => {
      if (editing) {
        // this.input && this.input.focus();
      }
    });
  }

  handleClickOutside(e) {
    const { editing } = this.state;
    // if (editing && this.cell !== e.target && !this.cell.contains(e.target)) {
    //   this.save();
    // }
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
    const { editable, record, children, index } = this.props;
    return (
      <td className="n2o-advanced-table-cell" onDoubleClick={this.toggleEdit}>
        <div className="n2o-advanced-table-cell-content">
          {record &&
            record.children &&
            index === 0 && <span className="n2o-advanced-table-indent" />}
          {editable &&
            editing && (
              <InputText
                value={this.state.value}
                disabled={false}
                onChange={this.onChange}
                autoFocus={true}
                onBlur={this.toggleEdit}
              />
            )}
          {children}
        </div>
      </td>
    );
  }
}

AdvancedTableCell.propTypes = {};

export default AdvancedTableCell;
