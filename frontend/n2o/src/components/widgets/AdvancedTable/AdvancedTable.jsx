import React, { Component } from 'react';
import PropTypes from 'prop-types';
import Table from 'rc-table';
import AdvancedTableExpandIcon from './AdvancedTableExpandIcon';
import AdvancedTableExpandedRenderer from './AdvancedTableExpandedRenderer';
import { HotKeys } from 'react-hotkeys';
import cx from 'classnames';
import propsResolver from '../../../utils/propsResolver';
import _, { find, isEqual, map, forOwn } from 'lodash';
import AdvancedTableRow from './AdvancedTableRow';
import AdvancedTableHeaderCell from './AdvancedTableHeaderCell';
import AdvancedTableEmptyText from './AdvancedTableEmptyText';
import TableCell from '../Table/TableCell';
import cn from 'classnames';
import { Resizable } from 'react-resizable';
import CheckboxN2O from '../../controls/Checkbox/CheckboxN2O';
import AdvancedTableCell from './AdvancedTableCell';
import Menu, { Item, Divider } from 'rc-menu';
import DropDown from 'rc-dropdown';
import 'rc-dropdown/assets/index.css';
import 'rc-menu/assets/index.css';
import AdvancedTableCellRenderer from './AdvancedTableCellRenderer';

export const getIndex = (data, selectedId) => {
  const index = _.findIndex(data, model => model.id == selectedId);
  return index >= 0 ? index : 0;
};

/**
 * columns: {
 *     title,
 *     dataIndex,
 *     id,
 *     index,
 *     model,
 *     key,
 *     width,
 *     fixed,
 *     sortable,
 *     sorting,
 *     resizable,
 *     editable,
 *     columnId,
 *     widgetId,
 *     onSort,
 * }
 */

/**
 * Компонент Таблица
 * @reactProps {boolean} hasFocus - флаг наличия фокуса
 * @reactProps {string} className - класс таблицы
 * @reactProps {Array.<Object>} columns - настройки колонок
 * @reactProps {Array.<Object>} data - данные
 * @reactProps {function} onRow - функция прокидывания дополнительных параметров в компонент строки
 * @reactProps {Object} components - компоненты обертки
 * @reactProps {Node} emptyText - компонент пустых данных
 * @reactProps {object} hotKeys - настройка hot keys
 */
class AdvancedTable extends Component {
  constructor(props) {
    super(props);

    this.state = {
      focusIndex: props.autoFocus ? getIndex(props.data, props.selectedId) : props.hasFocus ? 0 : 1,
      selectIndex: props.hasSelect ? getIndex(props.data, props.selectedId) : -1,
      data: props.data || [],
      expandedRowKeys: [],
      expandIconAsCell: true,
      expandRowByClick: false,
      selection: {},
      selectAll: false,
      columns: props.columns,
      checkedAll: false,
      checked: {}
    };

    this.rows = {};

    this.setRowRef = this.setRowRef.bind(this);
    this.getRowProps = this.getRowProps.bind(this);
    this.onKeyDown = this.onKeyDown.bind(this);
    this.onExpandedRowsChange = this.onExpandedRowsChange.bind(this);
    this.mapColumns = this.mapColumns.bind(this);
    this.checkAll = this.checkAll.bind(this);
    this.onChangeChecked = this.onChangeChecked.bind(this);
    this.onFilter = this.onFilter.bind(this);
  }

  componentDidUpdate(prevProps, prevState) {
    const { hasSelect, data, selectedId, isAnyTableFocused, isActive } = this.props;
    if (hasSelect && !isEqual(data, prevProps.data)) {
      const id = getIndex(data, selectedId);
      isAnyTableFocused && !isActive ? this.setNewSelectIndex(id) : this.setSelectAndFocus(id, id);
    }
    if (this.props.data && !isEqual(prevProps.data, this.props.data)) {
      const checked = {};
      map(this.props.data, item => {
        checked[item.id] = false;
      });
      this.setState({
        data: this.props.data,
        checked
      });
    }

    if (!isEqual(prevProps.columns, this.props.columns)) {
      this.setState({
        columns: this.props.columns
      });
    }
  }

  componentDidMount() {
    const { isAnyTableFocused, isActive, focusIndex, selectIndex } = this.state;
    !isAnyTableFocused && isActive && this.setSelectAndFocus(selectIndex, focusIndex);
    const checked = {};
    map(this.props.data, item => {
      checked[item.id] = false;
    });
    this.setState({
      data: this.props.data,
      columns: this.props.columns,
      checked
    });
  }

  setRowRef(ref, index) {
    if (ref) {
      this.rows[index] = ref;
    }
  }

  onKeyDown(e) {
    const keyNm = e.key;
    const { data, children, hasFocus, hasSelect, autoFocus } = this.props;
    const { focusIndex } = this.state;
    if (keyNm === 'ArrowUp' || keyNm === 'ArrowDown') {
      if (!React.Children.count(children) && hasFocus) {
        let newFocusIndex = keyNm === 'ArrowUp' ? focusIndex - 1 : focusIndex + 1;
        newFocusIndex =
          newFocusIndex < data.length && newFocusIndex >= 0 ? newFocusIndex : focusIndex;
        if (hasSelect && autoFocus) {
          this.setSelectAndFocus(newFocusIndex, newFocusIndex);
          this.props.onResolve(data[newFocusIndex]);
        } else {
          this.setNewFocusIndex(newFocusIndex);
        }
      }
    } else if (keyNm === ' ' && hasSelect && !autoFocus) {
      this.props.onResolve(data[this.state.focusIndex]);
      this.setNewSelectIndex(this.state.focusIndex);
    }
  }

  onFilter(filter) {
    const { onFilter } = this.props;
    onFilter && onFilter(filter);
  }

  handleRow(id, index, noResolve) {
    const { data, hasFocus, hasSelect } = this.props;
    hasSelect && !noResolve && this.props.onResolve(_.find(data, { id }));
    if (hasSelect && hasFocus) {
      this.setSelectAndFocus(index, index);
    } else if (hasFocus) {
      this.setNewFocusIndex(index);
    } else if (hasSelect) {
      this.setNewSelectIndex(index);
    }
  }

  setNewFocusIndex(index) {
    this.setState({ focusIndex: index }, () => this.focusActiveRow());
  }

  setNewSelectIndex(index) {
    this.setState({ selectIndex: index });
  }

  setSelectAndFocus(selectIndex, focusIndex) {
    this.setState({ selectIndex, focusIndex }, () => {
      this.focusActiveRow();
    });
  }

  focusActiveRow() {
    this.rows[this.state.focusIndex] && this.rows[this.state.focusIndex].focus();
  }

  onExpandedRowsChange(rows) {
    this.setState({
      expandedRowKeys: rows
    });
  }

  checkAll(event) {
    const { onSetSelection } = this.props;
    const checked = !event.target.checked;
    const newChecked = {};
    onSetSelection(checked ? _.toArray(this.props.data) : []);
    forOwn(this.state.checked, (v, k) => {
      newChecked[k] = checked;
    });
    this.setState(() => ({
      checkedAll: checked,
      checked: newChecked
    }));
  }

  onChangeChecked(event, index) {
    const { onSetSelection } = this.props;
    const checked = !event.target.checked;
    let multi = [];
    this.setState(
      () => ({
        checked: {
          ...this.state.checked,
          [index]: checked
        }
      }),
      () => {
        forOwn(this.state.checked, (v, k) => {
          console.log(v, k);
          if (v) {
            const item = find(this.props.data, i => i.id.toString() === k.toString());
            multi.push(item);
          }
        });
        onSetSelection(multi);
      }
    );
  }

  handleResize(index) {
    return (e, { size }) => {
      this.setState(({ columns }) => {
        const nextColumns = [...columns];
        nextColumns[index] = {
          ...nextColumns[index],
          width: size.width
        };
        return { columns: nextColumns };
      });
    };
  }

  getRowProps(model, index) {
    const { isActive, redux, rowColor } = this.props;
    return {
      index,
      redux,
      isRowActive: index === this.state.selectIndex,
      color: rowColor && propsResolver(rowColor, model),
      setRef: this.setRowRef
      // onClick: isActive ? () => this.handleRow(model.id, index) : undefined,
      // onFocus: !isActive ? () => this.handleRow(model.id, index, true) : undefined
    };
  }

  createSelectionColumn() {
    return {
      title: (
        <div className="n2o-advanced-table-selection-item">
          <CheckboxN2O inline={true} checked={this.state.checkedAll} onChange={this.checkAll} />
        </div>
      ),
      dataIndex: 'row-selection',
      key: 'row-selection',
      className: 'n2o-advanced-table-selection-container',
      width: 50,
      selectionHead: true,
      render: (value, model, index) => (
        <CheckboxN2O
          inline={true}
          checked={this.state.checked[model.id]}
          onChange={event => this.onChangeChecked(event, model.id)}
        />
      )
    };
  }

  mapColumns(columns) {
    const { widgetId, rowSelection, redux, sorting, onSort } = this.props;
    let newColumns = columns;

    newColumns = newColumns.map((col, index) => ({
      ...col,
      onHeaderCell: column => ({
        ...column,
        dataIndex: column.id,
        key: column.id,
        onResize: this.handleResize(index),
        onFilter: this.onFilter,
        columnId: column.id,
        widgetId,
        redux,
        sorting,
        onSort,
        component: col.header && !col.selectionHead && col.header.component,
        rowSelection: rowSelection
      }),
      onCell: (record, index) => ({
        editable: col.editable && record.editable,
        component: !col.selectionHead && col.cell.component
      }),
      render: (value, row, index) => (
        <AdvancedTableCellRenderer
          value={value}
          row={row}
          index={index}
          cell={col.cell}
          component={col.cell.component}
          columnId={col.id}
          widgetId={widgetId}
          redux={redux}
          id={col.id}
          width={col.width}
          editable={col.editable && row.editable}
          edit={col.edit}
        />
      )
    }));
    if (rowSelection) {
      newColumns = [this.createSelectionColumn(), ...newColumns];
    }
    return newColumns;
  }

  render() {
    const {
      hasFocus,
      className,
      expandable,
      onExpand,
      expandRowByClick,
      tableSize,
      useFixedHeader,
      scroll
    } = this.props;

    const columns = this.mapColumns(this.state.columns);

    return (
      <HotKeys keyMap={{ events: ['up', 'down', 'space'] }} handlers={{ events: this.onKeyDown }}>
        <Table
          prefixCls={'n2o-advanced-table'}
          className={cx('n2o-table table table-hover', className, {
            'has-focus': hasFocus,
            [`table-${tableSize}`]: tableSize
          })}
          columns={columns}
          data={this.state.data}
          onRow={this.getRowProps}
          components={{
            header: {
              row: AdvancedTableRow,
              cell: AdvancedTableHeaderCell
            },
            body: {
              row: AdvancedTableRow,
              cell: AdvancedTableCell
            }
          }}
          rowKey={record => record.key}
          expandIcon={AdvancedTableExpandIcon}
          // expandIconAsCell={true}
          expandRowByClick={expandRowByClick}
          expandedRowRender={expandable && AdvancedTableExpandedRenderer}
          expandedRowKeys={this.state.expandedRowKeys}
          onExpandedRowsChange={this.onExpandedRowsChange}
          onExpand={onExpand}
          useFixedHeader={useFixedHeader}
          indentSize={20}
          emptyText={AdvancedTableEmptyText}
          scroll={scroll}
        />
      </HotKeys>
    );
  }
}

AdvancedTable.propTypes = {
  hasFocus: PropTypes.bool,
  className: PropTypes.string,
  columns: PropTypes.arrayOf(PropTypes.object),
  data: PropTypes.arrayOf(PropTypes.object),
  onRow: PropTypes.func,
  components: PropTypes.object,
  emptyText: PropTypes.node,
  hotKeys: PropTypes.object
};

AdvancedTableCell.defaultProps = {
  data: []
};

export default AdvancedTable;
