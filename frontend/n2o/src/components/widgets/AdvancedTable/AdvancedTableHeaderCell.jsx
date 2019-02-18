import React, { Component } from 'react';
import { pick, isArray, isString } from 'lodash';
import cn from 'classnames';
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
    this.renderCell = this.renderCell.bind(this);
    this.renderMultiCell = this.renderMultiCell.bind(this);
    this.renderStringChild = this.renderStringChild.bind(this);
    this.renderSelectionBox = this.renderSelectionBox.bind(this);
  }

  handleVisibleChange(visible) {
    this.setState({ visible });
  }

  renderMultiCell() {
    const {
      colSpan,
      rowSpan,
      className,
      columnId,
      id,
      label,
      sorting,
      onSort,
      widgetId
    } = this.props;
    return (
      <th
        {...this.props}
        className={cn('n2o-advanced-table-header-text-center', className)}
        colSpan={colSpan}
        rowSpan={rowSpan}
      >
        {React.createElement(this.props.component, {
          className,
          columnId,
          id,
          label,
          sorting: sorting && sorting[id],
          onSort,
          widgetId
        })}
      </th>
    );
  }

  renderStringChild() {
    const { className, children } = this.props;
    return <th className={cn('n2o-advanced-table-header-text-center', className)}>{children}</th>;
  }

  renderSelectionBox() {
    const { children } = this.props;
    return children;
  }

  renderCell() {
    const {
      id,
      multiHeader,
      children,
      selectionHead,
      selectionClass,
      filterable,
      colSpan,
      rowSpan,
      onFilter,
      filters,
      component
    } = this.props;

    let cellContent = null;

    if (isString(children)) {
      return this.renderStringChild();
    } else if (multiHeader && isArray(children)) {
      return this.renderMultiCell();
    } else if (selectionHead) {
      cellContent = this.renderSelectionBox();
    } else {
      cellContent = children;
    }

    const cell = (
      <th
        {...this.props}
        rowSpan={rowSpan}
        colSpan={colSpan}
        className={cn('n2o-advanced-table-header-cel', { [selectionClass]: selectionHead })}
      >
        <div className="n2o-advanced-table-header-cell-content">
          {filterable ? (
            <AdvancedTableFilter
              id={id}
              onVisibleChange={this.handleVisibleChange}
              visible={this.state.visible}
              onFilter={onFilter}
              value={filters && filters[id]}
            >
              {cellContent}
            </AdvancedTableFilter>
          ) : (
            cellContent
          )}
        </div>
      </th>
    );

    return cell;
  }

  render() {
    const { width, onResize, resizable, children } = this.props;

    return (
      <React.Fragment>
        {resizable && width ? (
          <Resizable width={width} height={0} onResize={onResize} handleSize={[10, 10]}>
            {this.renderCell()}
          </Resizable>
        ) : (
          this.renderCell()
        )}
      </React.Fragment>
    );
  }
}

AdvancedTableHeaderCell.propTypes = {
  cell: PropTypes.func,
  children: PropTypes.oneOf(PropTypes.array, PropTypes.string, PropTypes.object),
  className: PropTypes.string,
  columnId: PropTypes.string,
  dataIndex: PropTypes.string,
  edit: PropTypes.func,
  editOptions: PropTypes.object,
  editable: PropTypes.bool,
  id: PropTypes.string,
  index: PropTypes.oneOf(PropTypes.string, PropTypes.number),
  label: PropTypes.string,
  multiHeader: PropTypes.bool,
  onCell: PropTypes.func,
  onFilter: PropTypes.func,
  onHeaderCell: PropTypes.func,
  onResize: PropTypes.func,
  onSort: PropTypes.func,
  redux: PropTypes.bool,
  render: PropTypes.func,
  sorting: PropTypes.object,
  title: PropTypes.oneOf(PropTypes.string, PropTypes.func),
  widgetId: PropTypes.string,
  width: PropTypes.number
};

AdvancedTableHeaderCell.defaultProps = {
  editable: false,
  multiHeader: false,
  redux: false,
  sorting: {},
  edit: null
};

export default AdvancedTableHeaderCell;
