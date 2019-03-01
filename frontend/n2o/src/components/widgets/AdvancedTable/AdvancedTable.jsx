import React, { Component } from 'react';
import ReactDom from 'react-dom';
import PropTypes from 'prop-types';
import Table from 'rc-table';
import AdvancedTableExpandIcon from './AdvancedTableExpandIcon';
import AdvancedTableExpandedRenderer from './AdvancedTableExpandedRenderer';
import { HotKeys } from 'react-hotkeys';
import cx from 'classnames';
import propsResolver from '../../../utils/propsResolver';
import _, {
  find,
  some,
  isEqual,
  isEmpty,
  map,
  forOwn,
  every,
  flattenDeep,
  isArray,
  get
} from 'lodash';
import AdvancedTableRow from './AdvancedTableRow';
import AdvancedTableHeaderCell from './AdvancedTableHeaderCell';
import AdvancedTableEmptyText from './AdvancedTableEmptyText';
import CheckboxN2O from '../../controls/Checkbox/CheckboxN2O';
import AdvancedTableCell from './AdvancedTableCell';
import AdvancedTableHeaderRow from './AdvancedTableHeaderRow';

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
 * @reactProps {any} expandedComponent - кастомный компонент подстроки
 */
class AdvancedTable extends Component {
  constructor(props) {
    super(props);

    this.state = {
      focusIndex: props.autoFocus
        ? props.data && props.data[props.selectedId]
          ? get(props.data[props.selectedId], 'id')
          : get(props.data[0], 'id')
        : props.hasFocus
          ? 0
          : 1,
      selectIndex: props.hasSelect ? getIndex(props.data, props.selectedId) : -1,
      data: props.data || [],
      expandedRowKeys: [],
      expandRowByClick: false,
      selection: {},
      selectAll: false,
      columns: props.columns,
      checkedAll: false,
      checked: props.data ? this.mapChecked(props.data) : {}
    };

    this.rows = {};
    this._dataStorage = [];

    this.setRowRef = this.setRowRef.bind(this);
    this.getRowProps = this.getRowProps.bind(this);
    this.handleKeyDown = this.handleKeyDown.bind(this);
    this.handleExpandedRowsChange = this.handleExpandedRowsChange.bind(this);
    this.mapColumns = this.mapColumns.bind(this);
    this.checkAll = this.checkAll.bind(this);
    this.handleChangeChecked = this.handleChangeChecked.bind(this);
    this.handleFilter = this.handleFilter.bind(this);
    this.handleEdit = this.handleEdit.bind(this);
    this.setSelectionRef = this.setSelectionRef.bind(this);
    this.getModelsFromData = this.getModelsFromData.bind(this);
    this.setTableRef = this.setTableRef.bind(this);
  }

  componentDidMount() {
    const { rowClick } = this.props;
    const { isAnyTableFocused, isActive, focusIndex, selectIndex, data } = this.state;
    !isAnyTableFocused &&
      isActive &&
      !rowClick &&
      this.setSelectAndFocus(get(data[selectIndex], 'id'), get(data[focusIndex], 'id'));
  }

  componentDidUpdate(prevProps, prevState) {
    const { hasSelect, data, isAnyTableFocused, isActive } = this.props;
    const { selectedId } = this.state;
    if (hasSelect && !isEmpty(data) && !isEqual(data, prevProps.data)) {
      const id =
        data && data[selectedId] && get(data[selectedId], 'id')
          ? get(data[selectedId], 'id')
          : data[0].id;
      isAnyTableFocused && !isActive ? this.setNewSelectIndex(id) : this.setSelectAndFocus(id, id);
    }
    if (!isEqual(prevProps, this.props)) {
      let state = {};
      if (this.props.data && !isEqual(prevProps.data, this.props.data)) {
        const checked = this.mapChecked(this.props.data);
        state = {
          data: isArray(data) ? data : [this.props.data],
          checked
        };
        this._dataStorage = this.getModelsFromData(this.props.data);
      }
      if (!isEqual(prevProps.columns, this.props.columns)) {
        state = {
          ...state,
          columns: this.props.columns
        };
      }
      this.setState({ ...state });
    }
  }

  mapChecked(data) {
    const checked = {};
    map(data, item => {
      checked[item.id] = false;
    });
    return checked;
  }

  getModelsFromData(data) {
    let dataStorage = [];
    const getChildren = children => {
      return map(children, model => {
        let array = [...children];
        if (model.children) {
          array = [...array, getChildren(model.children)];
        }
        return array;
      });
    };

    map(data, item => {
      if (item.children) {
        const children = getChildren(item.children);
        dataStorage.push(...flattenDeep(children));
      }
      dataStorage.push(item);
    });

    return dataStorage;
  }

  setTableRef(el) {
    this.table = el;
  }

  setSelectionRef(el) {
    this.selectAllCheckbox = el;
  }

  setRowRef(ref, id) {
    if (ref && !this.rows[id]) {
      this.rows[id] = ref;
    }
  }

  handleKeyDown(e) {
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

  handleFilter(filter) {
    const { onFilter } = this.props;
    onFilter && onFilter(filter);
  }

  handleRowClick(id, index, noResolve) {
    const { hasFocus, hasSelect, rowClick, onRowClickAction } = this.props;
    hasSelect && !noResolve && this.props.onResolve(_.find(this._dataStorage, { id }));
    if (hasSelect && hasFocus && !rowClick) {
      this.setSelectAndFocus(id, id);
    } else if (hasFocus) {
      this.setNewFocusIndex(id);
    } else if (hasSelect && !rowClick) {
      this.setNewSelectIndex(id);
    }
    if (rowClick) {
      onRowClickAction();
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

  handleExpandedRowsChange(rows) {
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

  handleChangeChecked(event, index) {
    const selectAllCheckbox = ReactDom.findDOMNode(this.selectAllCheckbox).querySelector('input');
    const { onSetSelection, data } = this.props;
    const checked = !event.target.checked;
    let checkedAll = this.state.checkedAll;
    let multi = [];
    const checkedState = {
      ...this.state.checked,
      [index]: checked
    };
    const isSomeOneChecked = some(checkedState, checked => checked);
    const isAllChecked = every(checkedState, checked => checked);
    if (isAllChecked) {
      checkedAll = true;
    }
    selectAllCheckbox.indeterminate = isSomeOneChecked && !isAllChecked;
    forOwn(checkedState, (v, k) => {
      if (v) {
        const item = find(data, i => i.id.toString() === k.toString());
        multi.push(item);
      }
    });
    onSetSelection(multi);
    this.setState(() => ({
      checked: checkedState,
      checkedAll
    }));
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

  handleEdit(value, index, id) {
    const { onEdit } = this.props;
    let data = this.state.data;
    data[index][id] = value;
    this.setState({
      data
    });
    onEdit(value, index, id);
  }

  getRowProps(model, index) {
    const { isActive, rowColor } = this.props;
    return {
      index,
      isRowActive: model.id === this.state.selectIndex,
      color: rowColor && propsResolver(rowColor, model),
      model,
      setRef: this.setRowRef,
      onClick: isActive ? () => this.handleRowClick(model.id, model.id) : undefined,
      onFocus: !isActive ? () => this.handleRowClick(model.id, model.id, true) : undefined
    };
  }

  createSelectionColumn() {
    const isSomeFixed = some(this.state.columns, c => c.fixed);
    return {
      title: (
        <div className="n2o-advanced-table-selection-item">
          <CheckboxN2O
            ref={this.setSelectionRef}
            inline={true}
            checked={this.state.checkedAll}
            onChange={this.checkAll}
          />
        </div>
      ),
      dataIndex: 'row-selection',
      key: 'row-selection',
      className: 'n2o-advanced-table-selection-container',
      width: 30,
      fixed: isSomeFixed && 'left',
      render: (value, model) => (
        <CheckboxN2O
          inline={true}
          checked={this.state.checked[model.id]}
          onChange={event => this.handleChangeChecked(event, model.id)}
        />
      )
    };
  }

  mapColumns(columns) {
    const { rowSelection, filters } = this.props;
    let newColumns = columns;
    newColumns = map(newColumns, (col, columnIndex) => ({
      ...col,
      onHeaderCell: column => ({
        ...column,
        onFilter: this.handleFilter,
        onResize: this.handleResize(columnIndex),
        filters
      }),
      onCell: record => ({
        record,
        editable: col.editable && record.editable,
        hasSpan: col.hasSpan
      })
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
      tableSize,
      useFixedHeader,
      scroll,
      bordered,
      isActive,
      onFocus,
      rowSelection,
      expandedFieldId,
      expandedComponent
    } = this.props;

    const columns = this.mapColumns(this.state.columns);
    return (
      <HotKeys
        keyMap={{ events: ['up', 'down', 'space'] }}
        handlers={{ events: this.handleKeyDown }}
      >
        <Table
          ref={this.setTableRef}
          onFocus={!isActive ? onFocus : undefined}
          prefixCls={'n2o-advanced-table'}
          className={cx('n2o-table table table-hover', className, {
            'has-focus': hasFocus,
            [`table-${tableSize}`]: tableSize,
            'table-bordered': bordered
          })}
          columns={columns}
          data={this.state.data}
          onRow={this.getRowProps}
          components={{
            header: {
              row: AdvancedTableHeaderRow,
              cell: AdvancedTableHeaderCell
            },
            body: {
              row: AdvancedTableRow,
              cell: AdvancedTableCell
            }
          }}
          rowKey={record => record.key}
          expandIcon={({ record, expanded, onExpand }) => (
            <AdvancedTableExpandIcon
              record={record}
              expanded={expanded}
              onExpand={onExpand}
              expandedFieldId={expandedFieldId}
              expandedComponent={expandedComponent}
            />
          )}
          expandIconAsCell={rowSelection && expandable}
          expandedRowRender={
            expandable &&
            (expandedComponent
              ? (record, index, indent) =>
                  React.createElement(expandedComponent, {
                    record,
                    index,
                    indent,
                    expandedFieldId
                  })
              : (record, index, indent) => (
                  <AdvancedTableExpandedRenderer
                    record={record}
                    index={index}
                    indent={indent}
                    expandedFieldId={expandedFieldId}
                  />
                ))
          }
          expandedRowKeys={this.state.expandedRowKeys}
          onExpandedRowsChange={this.handleExpandedRowsChange}
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
  hotKeys: PropTypes.object,
  bordered: PropTypes.bool,
  rowSelection: PropTypes.bool,
  expandable: PropTypes.bool,
  expandedFieldId: PropTypes.string,
  expandedComponent: PropTypes.any
};

AdvancedTable.defaultProps = {
  expandedFieldId: 'expandedContent',
  data: [],
  bordered: false,
  rowSelection: false,
  expandable: false,
  onFocus: () => {},
  onSetSelection: () => {}
};

export default AdvancedTable;
