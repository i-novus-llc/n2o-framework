import React, { Component } from 'react';
import PropTypes from 'prop-types';
import Table from 'rc-table';
import AdvancedTableExpandIcon from './AdvancedTableExpandIcon';
import AdvancedTableExpandedRenderer from './AdvancedTableExpandedRenderer';
import { HotKeys } from 'react-hotkeys';
import cx from 'classnames';
import propsResolver from '../../../utils/propsResolver';
import _, { find, isEqual } from 'lodash';
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

export const getIndex = (data, selectedId) => {
  const index = _.findIndex(data, model => model.id == selectedId);
  return index >= 0 ? index : 0;
};

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
    if (!isEqual(prevProps.data, this.props.data)) {
      const checked = {};
      Object.keys(this.props.data).map(key => {
        checked[key] = false;
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
    this.setState({
      data: this.props.data,
      columns: this.props.columns
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
    Object.keys(this.state.checked).map(key => (newChecked[key] = checked));
    this.setState(() => ({
      checkedAll: checked,
      checked: newChecked
    }));
  }

  onChangeChecked(event, index) {
    const checked = !event.target.checked;
    this.setState(() => ({
      checked: {
        ...this.state.checked,
        [index]: checked
      }
    }));
    const validIndexes = [
      ...Object.keys(this.state.checked).filter(key => this.state.checked[key] === true && key)
    ];
    if (checked) {
      validIndexes.push(index);
    }
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
    const { isActive, rowColor } = this.props;
    return {
      index,
      isRowActive: index === this.state.selectIndex,
      color: rowColor && propsResolver(rowColor, model),
      setRef: this.setRowRef,
      onClick: isActive ? () => this.handleRow(model.id, index) : undefined,
      onFocus: !isActive ? () => this.handleRow(model.id, index, true) : undefined
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
      render: (value, model, index) => (
        <td className="n2o-advanced-table-selection-item">
          <CheckboxN2O
            inline={true}
            checked={this.state.checked[index]}
            onChange={event => this.onChangeChecked(event, index)}
          />
        </td>
      )
    };
  }

  mapColumns(columns) {
    const { rowSelection } = this.props;
    let newColumns = columns;
    if (rowSelection) {
      newColumns = [this.createSelectionColumn(), ...newColumns];
    }
    newColumns = newColumns.map((col, index) => ({
      ...col,
      onHeaderCell: column => ({
        ...column,
        width: column.width,
        onResize: this.handleResize(index),
        onFilter: this.onFilter
      })
    }));

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
        <div className="n2o-advanced-table table-responsive">
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
                cell: ({ children }) => children
              }
            }}
            rowKey={record => record.key}
            expandIcon={AdvancedTableExpandIcon}
            expandIconAsCell={true}
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
        </div>
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

export default AdvancedTable;
