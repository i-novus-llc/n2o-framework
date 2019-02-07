import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Resizable } from 'react-resizable';
import 'react-resizable/css/styles.css';
import AdvancedTableFilter from './AdvancedTableFilter';

class AdvancedTableHeaderCell extends Component {
  constructor(props) {
    super(props);

    this.state = {
      visible: false
    };

    this.handleVisibleChange = this.handleVisibleChange.bind(this);
  }

  handleVisibleChange(visible) {
    this.setState({ visible });
  }

  render() {
    const {
      onResize,
      resizable,
      className,
      filterable,
      title,
      onFilter,
      id,
      ...restProps
    } = this.props;
    let width = this.props.width;
    if (className === 'n2o-advanced-table-expand-icon-th') {
      width = 10;
    } else if (className === 'n2o-advanced-table-selection-container') {
      width = 50;
    }
    const component = (
      <th {...this.props} width={width} className="n2o-advanced-table-header-cell">
        <div className="n2o-advanced-table-header-cell-content">
          {filterable ? (
            <AdvancedTableFilter
              id={id}
              onVisibleChange={this.handleVisibleChange}
              visible={this.state.visible}
              onFilter={onFilter}
            >
              {title}
            </AdvancedTableFilter>
          ) : (
            this.props.title
          )}
        </div>
      </th>
    );
    return !width || !resizable ? (
      component
    ) : (
      <Resizable width={this.props.width} height={0} onResize={onResize}>
        {component}
      </Resizable>
    );
  }
}

AdvancedTableHeaderCell.propTypes = {};

export default AdvancedTableHeaderCell;
