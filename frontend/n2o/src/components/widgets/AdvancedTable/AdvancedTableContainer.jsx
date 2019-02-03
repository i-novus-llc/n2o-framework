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
      selectIndex: props.hasSelect ? getIndex(props.datasource, props.selectedId) : -1
    };

    this.rows = {};

    this.getTableProps = this.getTableProps.bind(this);
    this.mapColumns = this.mapColumns.bind(this);
    this.mapData = this.mapData.bind(this);
    this.handleRow = this.handleRow.bind(this);
    this.setRowRef = this.setRowRef.bind(this);
    this.onKeyDown = this.onKeyDown.bind(this);
    this.getRowProps = this.getRowProps.bind(this);
  }

  componentDidUpdate(prevProps) {
    const { hasSelect, datasource, selectedId, isAnyTableFocused, isActive } = this.props;
    if (hasSelect && !isEqual(datasource, prevProps.datasource)) {
      const id = getIndex(datasource, selectedId);
      isAnyTableFocused && !isActive ? this.setNewSelectIndex(id) : this.setSelectAndFocus(id, id);
    }
  }

  componentDidMount() {
    const { isAnyTableFocused, isActive, focusIndex, selectIndex } = this.state;
    !isAnyTableFocused && isActive && this.setSelectAndFocus(selectIndex, focusIndex);
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

  renderCell(props) {
    const { redux } = this.props;

    const styleProps = pick(props, ['width']);

    if (redux) {
      return <ReduxCell style={styleProps} {...props} />;
    }
    return <TableCell style={styleProps} {...props} />;
  }

  mapColumns(headers) {
    if (!headers) return;
    const { widgetId, sorting, onSort, cells } = this.props;
    return headers.map(header => {
      const { id, label, component } = header;
      const cell = _.find(cells, c => c.id === id);
      const headerCell = component
        ? this.renderCell({
            key: id,
            columnId: id,
            widgetId,
            as: 'th',
            sorting: sorting && sorting[id],
            onSort: onSort,
            ...header
          })
        : label;
      const rowCell = (value, model, index) =>
        cell
          ? this.renderCell({
              index,
              key: cell.id,
              widgetId,
              columnId: cell.id,
              as: 'div',
              model,
              ...cell
            })
          : value;
      return {
        title: headerCell,
        dataIndex: id,
        key: id,
        render: rowCell
      };
    });
  }

  mapData(datasource) {
    if (!datasource) return;
    return datasource.map(item => {
      return {
        ...item,
        key: item.id
      };
    });
  }

  getRowProps(model, index) {
    const { isActive, rowColor } = this.props;
    return {
      index,
      isRowActive: this.state.selectIndex,
      color: rowColor && propsResolver(rowColor, model),
      setRef: this.setRowRef,
      onClick: isActive ? () => this.handleRow(model.id, index) : undefined,
      onFocus: !isActive ? () => this.handleRow(model.id, index, true) : undefined
    };
  }

  getTableProps() {
    const { headers, datasource, className, hasFocus } = this.props;

    return {
      className,
      hasFocus,
      columns: this.mapColumns(headers),
      data: this.mapData(datasource),
      onRow: this.getRowProps,
      components: {
        header: {
          row: AdvancedTableRow,
          cell: AdvancedTableCellRenderer
        },
        body: {
          row: AdvancedTableRow
        }
      },
      emptyText: AdvancedTableEmptyText,
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
          redux: true
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
