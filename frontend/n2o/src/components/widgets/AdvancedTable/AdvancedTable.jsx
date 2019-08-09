import React, { Component } from 'react';
import { compose, pure } from 'recompose';
import ReactDom from 'react-dom';
import PropTypes from 'prop-types';
import Table from 'rc-table';
import AdvancedTableExpandIcon from './AdvancedTableExpandIcon';
import AdvancedTableExpandedRenderer from './AdvancedTableExpandedRenderer';
import { HotKeys } from 'react-hotkeys/cjs';
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
  findIndex,
  values,
  eq,
  get,
  forEach,
  reduce,
  includes,
  has,
  isNumber,
} from 'lodash';
import AdvancedTableRow from './AdvancedTableRow';
import AdvancedTableHeaderCell from './AdvancedTableHeaderCell';
import AdvancedTableEmptyText from './AdvancedTableEmptyText';
import CheckboxN2O from '../../controls/Checkbox/CheckboxN2O';
import AdvancedTableCell from './AdvancedTableCell';
import AdvancedTableHeaderRow from './AdvancedTableHeaderRow';
import AdvancedTableSelectionColumn from './AdvancedTableSelectionColumn';
import withAdvancedTableRef from './withAdvancedTableRef';

export const getIndex = (data, selectedId) => {
  const index = _.findIndex(data, model => model.id == selectedId);
  return index >= 0 ? index : 0;
};

const KEY_CODES = {
  DOWN: 'down',
  UP: 'up',
  SPACE: 'space',
};

/**
 * Компонент Таблица
 * @reactProps {boolean} hasFocus - флаг наличия фокуса
 * @reactProps {string} className - класс таблицы
 * @reactProps {Array.<Object>} columns - настройки колонок
 * @reactProps {Array.<Object>} data - данные
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
      selectIndex: props.hasSelect
        ? getIndex(props.data, props.selectedId)
        : -1,
      data: props.data || [],
      expandedRowKeys: [],
      expandRowByClick: false,
      selection: {},
      selectAll: false,
      columns: [],
      checkedAll: false,
      checked: props.data ? this.mapChecked(props.data) : {},
    };

    this.rows = {};
    this._dataStorage = [];

    this.components = {
      header: {
        row: AdvancedTableHeaderRow,
        cell: AdvancedTableHeaderCell,
        ...get(props.components, 'header', {}),
      },
      body: {
        row: AdvancedTableRow,
        cell: AdvancedTableCell,
        ...get(props.components, 'body', {}),
      },
    };

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
    this.openAllRows = this.openAllRows.bind(this);
    this.closeAllRows = this.closeAllRows.bind(this);
    this.renderIcon = this.renderIcon.bind(this);
    this.renderExpandedRow = this.renderExpandedRow.bind(this);
    this.getScroll = this.getScroll.bind(this);
  }

  componentDidMount() {
    const { rowClick, columns } = this.props;
    const {
      isAnyTableFocused,
      isActive,
      focusIndex,
      selectIndex,
      data,
      autoFocus,
    } = this.state;
    if (!isAnyTableFocused && isActive && !rowClick && autoFocus) {
      this.setSelectAndFocus(
        get(data[selectIndex], 'id'),
        get(data[focusIndex], 'id')
      );
    }

    this.setState({
      columns: this.mapColumns(columns),
    });

    this._dataStorage = this.getModelsFromData(data);
  }

  componentDidUpdate(prevProps, prevState) {
    const {
      hasSelect,
      data,
      isAnyTableFocused,
      isActive,
      selectedId,
      autoFocus,
      columns,
    } = this.props;

    if (hasSelect && !isEmpty(data) && !isEqual(data, prevProps.data)) {
      const id = selectedId || data[0].id;
      if (isAnyTableFocused && !isActive) {
        this.setNewSelectIndex(id);
      } else if (autoFocus) {
        this.setSelectAndFocus(id, id);
      }
    }
    if (!isEqual(prevProps, this.props)) {
      let state = {};
      if (data && !isEqual(prevProps.data, data)) {
        const checked = this.mapChecked(data);
        state = {
          data: isArray(data) ? data : [data],
          checked,
        };
        this._dataStorage = this.getModelsFromData(data);
      }
      if (!isEqual(prevProps.columns, columns)) {
        state = {
          ...state,
          columns: this.mapColumns(columns),
        };
      }
      if (!isEqual(prevProps.selectedId, selectedId)) {
        this.setNewSelectIndex(selectedId);
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
    if (ref && ref !== this.rows[id]) {
      this.rows[id] = ref;
    }
  }

  handleKeyDown(e, keyName) {
    const { data, children, hasFocus, hasSelect, autoFocus } = this.props;
    const { focusIndex } = this.state;

    const modelIndex = findIndex(data, i => i.id === focusIndex);

    if (eq(keyName, KEY_CODES.UP) || eq(keyName, KEY_CODES.DOWN)) {
      if (!React.Children.count(children) && hasFocus) {
        const newFocusIndex = eq(keyName, KEY_CODES.UP)
          ? modelIndex - 1
          : modelIndex + 1;

        if (newFocusIndex >= data.length || newFocusIndex < 0) return false;
        const nextData = data[newFocusIndex];
        if (hasSelect && autoFocus) {
          this.setSelectAndFocus(nextData.id, nextData.id);
          this.props.onResolve(nextData);
        } else {
          this.setNewFocusIndex(nextData.id);
        }
      }
    } else if (eq(keyName, KEY_CODES.SPACE)) {
      if (hasSelect && !autoFocus) {
        this.props.onResolve(data[modelIndex]);
        this.setNewSelectIndex(focusIndex);
      }
    }
  }

  handleFilter(filter) {
    const { onFilter } = this.props;
    onFilter && onFilter(filter);
  }

  handleRowClick(id, index, needReturn, noResolve) {
    const {
      hasFocus,
      hasSelect,
      rowClick,
      onRowClickAction,
      onResolve,
      isActive,
    } = this.props;
    const needToReturn = isActive === needReturn;

    if (!needToReturn && hasSelect && !noResolve) {
      onResolve(_.find(this._dataStorage, { id }));
    }

    if (!noResolve && rowClick) {
      !hasSelect && onResolve(_.find(this._dataStorage, { id }));
      onRowClickAction();
    }

    if (needToReturn) return;

    if (!noResolve && hasSelect && hasFocus && !rowClick) {
      this.setSelectAndFocus(id, id);
    } else if (hasFocus) {
      this.setNewFocusIndex(id);
    } else if (hasSelect && !rowClick) {
      this.setNewSelectIndex(id);
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
    this.rows[this.state.focusIndex] &&
      this.rows[this.state.focusIndex].focus();
  }

  openAllRows() {
    const { data } = this.props;
    const keys = [];
    const getKeys = array => {
      return map(array, item => {
        keys.push(item.id);
        if (item.children) {
          getKeys(item.children);
        }
      });
    };
    getKeys(data);
    this.setState({
      expandedRowKeys: keys,
    });
  }

  closeAllRows() {
    this.setState({
      expandedRowKeys: [],
    });
  }

  handleExpandedRowsChange(rows) {
    this.setState({
      expandedRowKeys: rows,
    });
  }

  checkAll(checked) {
    const { onSetSelection } = this.props;
    const newChecked = {};
    onSetSelection(checked ? _.toArray(this.props.data) : []);
    forOwn(this.state.checked, (v, k) => {
      newChecked[k] = checked;
    });
    this.setState(() => ({
      checkedAll: checked,
      checked: newChecked,
    }));
  }

  handleChangeChecked(event, index) {
    const selectAllCheckbox = ReactDom.findDOMNode(
      this.selectAllCheckbox
    ).querySelector('input');
    const { onSetSelection, data } = this.props;
    const checked = !event.target.checked;
    let checkedAll = this.state.checkedAll;
    let multi = [];
    const checkedState = {
      ...this.state.checked,
      [index]: checked,
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
      checkedAll,
    }));
  }

  handleResize(index) {
    return (e, { size }) => {
      this.setState(({ columns }) => {
        const nextColumns = [...columns];
        nextColumns[index] = {
          ...nextColumns[index],
          width: size.width,
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
      data,
    });
    onEdit(value, index, id);
  }

  getRowProps(model, index) {
    const { rowClick, rowClass } = this.props;
    return {
      index,
      rowClick,
      isRowActive: model.id === this.state.selectIndex,
      rowClass: rowClass && propsResolver(rowClass, model),
      model,
      setRef: this.setRowRef,
      onClick: () => this.handleRowClick(model.id, model.id, false),
      onFocus: () => this.handleRowClick(model.id, model.id, true, true),
    };
  }

  createSelectionColumn(columns) {
    const isSomeFixed = some(columns, c => c.fixed);
    return {
      title: (
        <AdvancedTableSelectionColumn
          setRef={this.setSelectionRef}
          onChange={this.checkAll}
        />
      ),
      dataIndex: 'row-selection',
      key: 'row-selection',
      className: 'n2o-advanced-table-selection-container',
      width: 30,
      fixed: isSomeFixed && 'left',
      render: (value, model) => (
        <CheckboxN2O
          className="n2o-advanced-table-row-checkbox"
          inline={true}
          checked={this.state.checked[model.id]}
          onChange={event => this.handleChangeChecked(event, model.id)}
        />
      ),
    };
  }

  getRowKey(row) {
    return row.key;
  }

  renderIcon({ record, expanded, onExpand }) {
    const { expandedFieldId, expandedComponent } = this.props;

    return (
      <AdvancedTableExpandIcon
        record={record}
        expanded={expanded}
        onExpand={onExpand}
        expandedFieldId={expandedFieldId}
        expandedComponent={expandedComponent}
      />
    );
  }

  renderExpandedRow() {
    const { expandable, expandedComponent, expandedFieldId } = this.props;

    return (
      expandable &&
      (expandedComponent
        ? (record, index, indent) =>
            React.createElement(expandedComponent, {
              record,
              index,
              indent,
              expandedFieldId,
            })
        : (record, index, indent) => (
            <AdvancedTableExpandedRenderer
              record={record}
              index={index}
              indent={indent}
              expandedFieldId={expandedFieldId}
            />
          ))
    );
  }

  mapColumns(columns = []) {
    const { rowSelection, filters } = this.props;
    let newColumns = columns;
    newColumns = map(newColumns, (col, columnIndex) => ({
      ...col,
      onHeaderCell: column => ({
        ...column,
        onFilter: this.handleFilter,
        onResize: this.handleResize(columnIndex),
        filters,
      }),
      onCell: record => ({
        record,
        editable: col.editable && record.editable,
        hasSpan: col.hasSpan,
      }),
    }));
    if (rowSelection) {
      newColumns = [this.createSelectionColumn(columns), ...newColumns];
    }
    return newColumns;
  }

  getScroll() {
    if (!some(this.state.columns, col => has(col, 'fixed')))
      return this.props.scroll;
    const { scroll, columns } = this.props;
    const calcXScroll = () => {
      const getWidth = (
        separator,
        startValue,
        defaultWidth,
        tryParse = false
      ) =>
        reduce(
          columns,
          (result, value) => {
            if (value.width) {
              return includes(value.width, separator) ||
                (tryParse && isNumber(value.width))
                ? result + parseInt(value.width)
                : result;
            } else {
              return result + defaultWidth;
            }
          },
          startValue
        );

      const pxWidth = getWidth('px', 5, 100, true);
      const percentWidth = getWidth('%', 0, 0);

      return percentWidth !== 0
        ? `calc(${percentWidth}%${pxWidth > 5 ? ` + ${pxWidth}px` : ''})`
        : pxWidth;
    };

    return {
      ...scroll,
      x: calcXScroll(),
    };
  }

  render() {
    const {
      hasFocus,
      className,
      expandable,
      onExpand,
      tableSize,
      useFixedHeader,
      bordered,
      isActive,
      onFocus,
      rowSelection,
    } = this.props;

    return (
      <HotKeys
        keyMap={{ events: values(KEY_CODES) }}
        handlers={{ events: this.handleKeyDown }}
      >
        <div onFocus={!isActive ? onFocus : undefined}>
          <Table
            ref={this.setTableRef}
            prefixCls={'n2o-advanced-table'}
            className={cx('n2o-table table table-hover', className, {
              'has-focus': hasFocus,
              [`table-${tableSize}`]: tableSize,
              'table-bordered': bordered,
            })}
            columns={this.state.columns}
            data={this.state.data}
            onRow={this.getRowProps}
            components={this.components}
            rowKey={this.getRowKey}
            expandIcon={this.renderIcon}
            expandIconAsCell={rowSelection && expandable}
            expandedRowRender={this.renderExpandedRow()}
            expandedRowKeys={this.state.expandedRowKeys}
            onExpandedRowsChange={this.handleExpandedRowsChange}
            onExpand={onExpand}
            useFixedHeader={useFixedHeader}
            indentSize={20}
            emptyText={AdvancedTableEmptyText}
            scroll={this.getScroll()}
          />
        </div>
      </HotKeys>
    );
  }
}

AdvancedTable.propTypes = {
  /**
   * Наличие фокуса на строке при клике
   */
  hasFocus: PropTypes.bool,
  /**
   * Класс
   */
  className: PropTypes.string,
  /**
   * Массив колонок
   */
  columns: PropTypes.arrayOf(PropTypes.object),
  /**
   * Данные
   */
  data: PropTypes.arrayOf(PropTypes.object),
  /**
   * Кастомные компоненты
   */
  components: PropTypes.object,
  /**
   * Флаг включения border у таблицы
   */
  bordered: PropTypes.bool,
  /**
   * Флаг включения выбора строк
   */
  rowSelection: PropTypes.bool,
  /**
   * Флаг включения саб контента
   */
  expandable: PropTypes.bool,
  /**
   * Ключ к саб контенту в данных
   */
  expandedFieldId: PropTypes.string,
  /**
   * Кастомный компонент саб строки
   */
  expandedComponent: PropTypes.any,
  /**
   * Автофокус на строке
   */
  autoFocus: PropTypes.bool,
};

AdvancedTable.defaultProps = {
  expandedFieldId: 'expandedContent',
  data: [],
  bordered: false,
  tableSize: 'sm',
  rowSelection: false,
  expandable: false,
  onFocus: () => {},
  onSetSelection: () => {},
  autoFocus: false,
};

export default compose(
  pure,
  withAdvancedTableRef
)(AdvancedTable);
