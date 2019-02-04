import React from 'react';
import { connect } from 'react-redux';
import { lifecycle, compose } from 'recompose';
import { isEqual, find, isEmpty, debounce, pick } from 'lodash';
import AdvancedTable from './AdvancedTable';
import AdvancedTableEmptyText from './AdvancedTableEmptyText';
import widgetContainer from '../WidgetContainer';
import { setTableSelectedId } from '../../../actions/widgets';
import { ADVANCED_TABLE } from '../widgetTypes';
import _ from 'lodash';
import columnHOC from '../Table/ColumnContainer';
import TableCell from '../Table/TableCell';
import propsResolver from '../../../utils/propsResolver';
import AdvancedTableRow from './AdvancedTableRow';
import AdvancedTableCellRenderer from './AdvancedTableCellRenderer';
import AdvancedTableSelectionBox from './AdvancedTableSelectionBox';
import AdvancedTableHeaderCell from './AdvancedTableHeaderCell';
import CheckboxCell from '../Table/cells/CheckboxCell/CheckboxCell';
import N2OCheckbox from '../../controls/Checkbox/CheckboxN2O';

const isEqualCollectionItemsById = (data1 = [], data2 = [], selectedId) => {
  const predicate = ({ id }) => id == selectedId;
  return isEqual(find(data1, predicate), find(data2, predicate));
};

export const getIndex = (datasource, selectedId) => {
  const index = _.findIndex(datasource, model => model.id == selectedId);
  return index >= 0 ? index : 0;
};

const ReduxCell = columnHOC(TableCell);

class AdvancedTableContainer extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      focusIndex: props.autoFocus
        ? getIndex(props.datasource, props.selectedId)
        : props.hasFocus
          ? 0
          : 1,
      selectIndex: props.hasSelect ? getIndex(props.datasource, props.selectedId) : -1,
      data: this.mapData(props.datasource),
      expandedRowKeys: [],
      expandIconAsCell: true,
      expandRowByClick: false,
      selection: {},
      selectAll: false
    };

    this.rows = {};

    this.getTableProps = this.getTableProps.bind(this);
    this.mapColumns = this.mapColumns.bind(this);
    this.mapData = this.mapData.bind(this);
    this.handleRow = this.handleRow.bind(this);
    this.setRowRef = this.setRowRef.bind(this);
    this.onKeyDown = this.onKeyDown.bind(this);
    this.getRowProps = this.getRowProps.bind(this);
    this.onExpandedRowsChange = this.onExpandedRowsChange.bind(this);
    this.selectCheckboxRow = this.selectCheckboxRow.bind(this);
  }

  componentDidUpdate(prevProps, prevState) {
    const { hasSelect, datasource, selectedId, isAnyTableFocused, isActive } = this.props;
    if (hasSelect && !isEqual(datasource, prevProps.datasource)) {
      const id = getIndex(datasource, selectedId);
      isAnyTableFocused && !isActive ? this.setNewSelectIndex(id) : this.setSelectAndFocus(id, id);
    }
    if (!isEqual(prevProps.datasource, this.props.datasource)) {
      this.setState({
        data: this.mapData(this.props.datasource)
      });
    }
  }

  componentDidMount() {
    const { isAnyTableFocused, isActive, focusIndex, selectIndex } = this.state;
    !isAnyTableFocused && isActive && this.setSelectAndFocus(selectIndex, focusIndex);
    this.setState({
      data: this.mapData(this.props.datasource)
    });
  }

  setRowRef(ref, index) {
    if (ref) {
      this.rows[index] = ref;
    }
  }

  onKeyDown(e) {
    const keyNm = e.key;
    const { datasource, children, hasFocus, hasSelect, autoFocus } = this.props;
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

  handleRow(id, index, noResolve) {
    const { datasource, hasFocus, hasSelect } = this.props;
    hasSelect && !noResolve && this.props.onResolve(_.find(datasource, { id }));
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

  renderCell(props, isRowCell, isFirstColumn = false) {
    const { redux, rowSelection } = this.props;

    const styleProps = pick(props, ['width']);

    return (
      <React.Fragment>
        {/*{isRowCell && rowSelection && isFirstColumn && (<CheckboxCell />)}*/}
        {redux ? (
          <ReduxCell style={styleProps} {...props} />
        ) : (
          <TableCell style={styleProps} {...props} />
        )}
      </React.Fragment>
    );
  }

  renderRowSelection(value, model, index) {
    const rowKey = model.key;
    const { redux, rowSelection } = this.props;
    const styleProps = pick(this.props, ['width']);
    const cell = redux ? (
      <ReduxCell style={styleProps} {...this.props} />
    ) : (
      <TableCell style={styleProps} {...this.props} />
    );

    return (
      <span>
        <AdvancedTableSelectionBox />
      </span>
    );
  }

  selectCheckboxRow(index, event) {
    const checked = event.target.checked;
    this.setState(state => {
      return {
        selection: {
          ...state.selection,
          [index]: !checked
        }
      };
    });
  }

  selectAllToggle(event) {
    //TODO чекать и снимать чек со всех строк + делать это по id а не index
  }

  mapColumns() {
    let { headers, rowSelection } = this.props;
    if (!headers) return;
    if (rowSelection) {
      headers = [
        {
          label: (
            <th>
              <N2OCheckbox
                inline={true}
                checked={this.state.selectAll}
                onChange={event => this.selectAllToggle(event)}
              />
            </th>
          ),
          dataIndex: 'name',
          key: 'selection-column',
          render: (value, model, index) => (
            <div>
              <N2OCheckbox
                onChange={this.selectCheckboxRow.bind(null, index)}
                checked={this.state.selection[index] || this.state.selectAll}
                inline={true}
              />
            </div>
          )
        },
        ...headers
      ];
    }
    const { widgetId, sorting, onSort, cells } = this.props;
    return headers.map((header, index) => {
      const { id, label, component } = header;
      const cell = _.find(cells, c => c.id === id);
      const isFirstColumn = index === 0;
      const headerCell = component
        ? this.renderCell(
            {
              key: id,
              columnId: id,
              widgetId,
              as: 'th',
              sorting: sorting && sorting[id],
              onSort: onSort,
              ...header
            },
            false
          )
        : label;
      const rowCell = (value, model, index) => ({
        children: cell
          ? this.renderCell(
              {
                index,
                key: cell.id,
                widgetId,
                columnId: cell.id,
                as: 'div',
                model,
                ...cell
              },
              true,
              isFirstColumn
            )
          : value,
        props: {
          colSpan: model.colSpan || null,
          rowSpan: model.rowSpan || null
        }
      });
      return {
        ...header,
        title: headerCell,
        dataIndex: id,
        key: id,
        render: header.render || rowCell
      };
    });
  }

  mapData(datasource) {
    if (!datasource) return;
    return datasource.map(item => {
      return {
        ...item,
        key: item.id,
        children: [
          {
            key: item.id + 21,
            name: 'test',
            children: [
              {
                key: item.id + 22,
                name: 'ttt'
              }
            ]
          }
        ]
      };
    });
  }

  onExpandedRowsChange(rows) {
    this.setState({
      expandedRowKeys: rows
    });
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

  getTableProps() {
    const { columns, datasource, tableSize, className, hasFocus, useFixedHeader } = this.props;

    return {
      className,
      hasFocus,
      columns: columns || this.mapColumns(),
      data: this.state.data,
      onRow: this.getRowProps,
      components: {
        header: {
          row: AdvancedTableRow,
          cell: AdvancedTableHeaderCell
        },
        body: {
          row: AdvancedTableRow
        }
      },
      emptyText: AdvancedTableEmptyText,
      rowKey: record => record.key,
      onExpand: this.onExpand,
      expandIconAsCell: this.state.expandIconAsCell,
      expandRowByClick: this.state.expandRowByClick,
      onExpandedRowsChange: this.onExpandedRowsChange,
      expandedRowKeys: this.state.expandedRowKeys,
      tableSize,
      useFixedHeader,
      hotKeys: {
        keyMap: {
          events: ['up', 'down', 'space']
        },
        handlers: {
          events: this.onKeyDown
        }
      }
    };
  }

  render() {
    return <AdvancedTable {...this.getTableProps()} />;
  }
}

export default compose(
  widgetContainer(
    {
      mapProps: props => {
        return {
          widgetId: props.widgetId,
          pageId: props.pageId,
          headers: props.headers,
          cells: props.cells,
          isAnyTableFocused: props.isAnyTableFocused,
          isActive: props.isActive,
          hasFocus: props.hasFocus,
          hasSelect: props.hasSelect,
          autoFocus: props.autoFocus,
          datasource: props.datasource,
          selectedId: props.selectedId,
          sorting: props.sorting,
          rowColor: props.rowColor,
          onSort: props.onSort,
          onResolve: debounce(newModel => {
            props.onResolve(newModel);
            if (props.selectedId != newModel.id) {
              props.dispatch(setTableSelectedId(props.widgetId, newModel.id));
            }
          }, 100),
          onFocus: props.onFocus,
          size: props.size,
          actions: props.actions,
          redux: true,
          rowSelection: props.rowSelection,
          tableSize: props.tableSize,
          useFixedHeader: props.useFixedHeader
        };
      }
    },
    ADVANCED_TABLE
  ),
  lifecycle({
    componentWillReceiveProps(nextProps) {
      const { selectedId: prevSelectedId, datasource: prevDatasource, onResolve } = this.props;
      const { hasSelect, datasource, selectedId } = nextProps;

      if (
        hasSelect &&
        !isEmpty(datasource) &&
        !isEqual(prevDatasource, datasource) &&
        (!selectedId ||
          !isEqual(prevSelectedId, selectedId) ||
          !isEqualCollectionItemsById(prevDatasource, datasource, selectedId))
      ) {
        const selectedModel = find(datasource, model => model.id == selectedId);
        const resolveModel = selectedModel || datasource[0];
        onResolve(resolveModel);
      }
    }
  })
)(AdvancedTableContainer);
