import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Resizable } from 'react-resizable';
import 'react-resizable/css/styles.css';
import N2OCheckbox from '../../controls/Checkbox/CheckboxN2O';

class AdvancedTableHeaderCell extends Component {
  render() {
    const { children } = this.props;

    if (children && children.props.children && children.props.children.type !== N2OCheckbox) {
      return children.props.children;
    }

    return <th>{children}</th>;
  }
}

AdvancedTableHeaderCell.propTypes = {};

export default AdvancedTableHeaderCell;
