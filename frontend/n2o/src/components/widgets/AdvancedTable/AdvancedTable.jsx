import React, { Component } from 'react';
import PropTypes from 'prop-types';
import AdvancedCellRenderer from './AdvancedTableCellRenderer';
import AdvancedTableRow from './AdvancedTableRow';
import Table from 'rc-table';
import { HotKeys } from 'react-hotkeys';
import _ from 'lodash';
import ReactDOM from 'react-dom';

export const getIndex = (datasource, selectedId) => {
  const index = _.findIndex(datasource, model => model.id == selectedId);
  return index >= 0 ? index : 0;
};

class AdvancedTable extends Component {
  constructor(props) {
    super(props);

    this.state = {
      focusIndex: props.autoFocus
        ? getIndex(props.datasource, props.selectedId)
        : props.hasFocus
          ? 0
          : 1,
      selectIndex: props.hasSelect ? getIndex(props.datasource, props.selectedId) : -1
    };

    this.prepareColumns = this.prepareColumns.bind(this);
    this.prepareData = this.prepareData.bind(this);
    this.onKeyDown = this.onKeyDown.bind(this);
    this.onClick = this.onClick.bind(this);
  }

  onKeyDown(e) {
    const keyNm = e.key;
    const { datasource, children, hasFocus, hasSelect, autoFocus, onResolve } = this.props;
    const { focusIndex } = this.state;
    if (keyNm === 'ArrowUp' || keyNm === 'ArrowDown') {
      if (!React.Children.count(children) && hasFocus) {
        let newFocusIndex = keyNm === 'ArrowUp' ? focusIndex - 1 : focusIndex + 1;
        newFocusIndex =
          newFocusIndex < datasource.length && newFocusIndex >= 0 ? newFocusIndex : focusIndex;
        if (hasSelect && autoFocus) {
          this.setSelectAndFocus(newFocusIndex, newFocusIndex);
          this.props.onResolve(datasource[newFocusIndex]);
        } else {
          this.setNewFocusIndex(newFocusIndex);
        }
      }
    } else if (keyNm === ' ' && hasSelect && !autoFocus) {
      this.props.onResolve(datasource[this.state.focusIndex]);
      this.setNewSelectIndex(this.state.focusIndex);
    }
  }

  prepareColumns(columns) {
    return columns.map(({ id, label, sortable }) => {
      return {
        title: label,
        dataIndex: id,
        key: id
      };
    });
  }

  prepareData(datasource) {
    if (!datasource) return;
    return datasource.map(item => {
      return {
        ...item,
        key: item.id
      };
    });
  }

  onClick(ref) {
    setTimeout(() => {
      ref.focus();
    }, 1);
  }

  render() {
    const { headers, datasource } = this.props;
    return (
      <HotKeys keyMap={{ events: ['up', 'down', 'space'] }} handlers={{ events: this.onKeyDown }}>
        <div className="n2o-advanced-table">
          <Table
            className="n2o-table table table-sm"
            columns={this.prepareColumns(headers)}
            data={this.prepareData(datasource)}
            onRow={props => ({
              ...props,
              onClick: this.onClick
            })}
            components={{
              body: {
                row: AdvancedTableRow
              }
            }}
          />
        </div>
      </HotKeys>
    );
  }
}

AdvancedTable.propTypes = {};

export default AdvancedTable;
