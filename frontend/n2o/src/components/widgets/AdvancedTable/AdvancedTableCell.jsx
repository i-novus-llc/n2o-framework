import React, { Component } from 'react';
import PropTypes from 'prop-types';

class AdvancedTableCell extends Component {
  constructor(props) {
    super(props);

    this.state = {
      editing: false
    };

    this.toggleEdit = this.toggleEdit.bind(this);
    this.handleClickOutside = this.handleClickOutside.bind(this);
  }

  componentDidMount() {
    if (this.props.editable) {
      document.addEventListener('click', this.handleClickOutside, true);
    }
  }

  componentWillUnmount() {
    if (this.props.editable) {
      document.removeEventListener('click', this.handleClickOutside, true);
    }
  }

  toggleEdit() {
    const editing = !this.state.editing;
    this.setState({ editing }, () => {
      if (editing) {
        this.input.focus();
      }
    });
  }

  handleClickOutside(e) {
    const { editing } = this.state;
    if (editing && this.cell !== e.target && !this.cell.contains(e.target)) {
      this.save();
    }
  }

  save() {
    console.log('test');
  }

  render() {
    const { editing } = this.state;
    const { editable, record, children, index } = this.props;
    return (
      <td className="n2o-advanced-table-cell">
        {record && record.children && index === 0 && <span className="n2o-advanced-table-indent" />}
        {editable ? children : children}
      </td>
    );
  }
}

AdvancedTableCell.propTypes = {};

export default AdvancedTableCell;
