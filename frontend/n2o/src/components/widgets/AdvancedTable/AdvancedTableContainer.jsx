import React from 'react';
import { connect } from 'react-redux';
import { lifecycle, compose } from 'recompose';
import { isEqual, find, isEmpty, debounce, pick, forOwn } from 'lodash';
import AdvancedTable from './AdvancedTable';
import AdvancedTableEmptyText from './AdvancedTableEmptyText';
import widgetContainer from '../WidgetContainer';
import { setTableSelectedId } from '../../../actions/widgets';
import { ADVANCED_TABLE } from '../widgetTypes';
import _ from 'lodash';
import columnHOC from '../Table/ColumnContainer';
import TableCell from '../Table/TableCell';
import { setModel } from '../../../actions/models';
import { PREFIXES } from '../../../constants/models';
import { Resizable } from 'react-resizable';
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
      data: this.mapData(props.datasource)
    };

    this._timeoutId = null;

    this.getTableProps = this.getTableProps.bind(this);
    this.mapColumns = this.mapColumns.bind(this);
    this.mapData = this.mapData.bind(this);
    this.renderHeaderCell = this.renderHeaderCell.bind(this);
    this.onSetFilter = this.onSetFilter.bind(this);
  }

  componentDidUpdate(prevProps, prevState) {
    if (!isEqual(prevProps.datasource, this.props.datasource)) {
      this.setState({
        data: this.mapData(this.props.datasource)
      });
    }
  }

  componentDidMount(prevProps) {
    if (this.props.datasource) {
      this.setState({
        data: this.mapData(this.props.datasource)
      });
    }
  }

  renderHeaderCell(props) {
    if (props.children) return props.label;
    const { redux } = this.props;
    const propStyles = pick(this.props, ['width']);
    let component = null;
    if (redux) {
      component = <ReduxCell {...propStyles} {...props} label={props.title} as={'div'} />;
    } else {
      component = <TableCell {...propStyles} {...props} label={props.title} as={'div'} />;
    }

    return component;
  }

  onSetFilter(filter) {
    const { onSetFilter, onFetch } = this.props;
    this._filter = {
      ...this._filter,
      [filter.id]: filter.value
    };
    forOwn(this._filter, (v, k) => {
      if (!v || isEmpty(v)) delete this._filter[k];
    });
    clearTimeout(this._timeoutId);
    onSetFilter({ ...this._filter });
    this._timeoutId = setTimeout(() => onFetch(), 500);
  }

  mapColumns() {
    const { columns, cells, headers, widgetId, sorting, onSort } = this.props;
    if (columns) {
    } else {
      return headers.map((header, index) => {
        const cell = find(cells, c => c.id === header.id);
        return {
          ...header,
          // title: this.renderHeaderCell({
          //   key: header.id,
          //   columnId: header.id,
          //   widgetId,
          //   as: 'th',
          //   sorting: sorting && sorting[header.id],
          //   onSort,
          //   ...header
          // }),
          dataIndex: header.id,
          key: header.id,
          render: (value, record, i) => (
            <AdvancedTableCellRenderer
              value={value}
              record={record}
              index={i}
              editable={record.editable}
              redux={this.props.redux}
              propsStyles={pick(this.props, ['width'])}
              props={{
                index,
                key: cell.id,
                widgetId,
                columnId: cell.id,
                model: record,
                ...cell
              }}
              key={i}
            />
          )
        };
      });
    }
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

  getTableProps() {
    const {
      columns,
      scroll,
      datasource,
      tableSize,
      className,
      hasFocus,
      useFixedHeader,
      rowColor,
      rowSelection,
      expandable,
      hasSelect,
      onSetSelection,
      isActive
    } = this.props;
    return {
      className,
      hasFocus,
      rowColor,
      columns: columns || this.mapColumns(),
      data: this.state.data,
      tableSize,
      rowSelection,
      useFixedHeader,
      scroll,
      expandable,
      hasSelect,
      onSetSelection,
      onFilter: this.onSetFilter,
      isActive
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
          onFetch: props.onFetch,
          onSort: props.onSort,
          onResolve: debounce(newModel => {
            props.onResolve(newModel);
            if (props.selectedId != newModel.id) {
              props.dispatch(setTableSelectedId(props.widgetId, newModel.id));
            }
          }, 100),
          onSetSelection: model => {
            props.dispatch(setModel(PREFIXES.multi, props.widgetId, model));
          },
          onSetFilter: filters => {
            props.dispatch(setModel(PREFIXES.filter, props.widgetId, filters));
          },
          onFocus: props.onFocus,
          size: props.size,
          actions: props.actions,
          redux: true,
          rowSelection: props.rowSelection,
          tableSize: props.tableSize,
          useFixedHeader: props.useFixedHeader,
          expandable: props.expandable,
          scroll: props.scroll
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
