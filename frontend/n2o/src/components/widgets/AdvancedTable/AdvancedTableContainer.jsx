import React from 'react';
import { connect } from 'react-redux';
import { lifecycle, compose } from 'recompose';
import { map, isEqual, find, isEmpty, debounce, pick, forOwn } from 'lodash';
import AdvancedTable from './AdvancedTable';
import widgetContainer from '../WidgetContainer';
import { setTableSelectedId } from '../../../actions/widgets';
import { ADVANCED_TABLE } from '../widgetTypes';
import _ from 'lodash';
import columnHOC from '../Table/ColumnContainer';
import TableCell from '../Table/TableCell';
import { setModel } from '../../../actions/models';
import { PREFIXES } from '../../../constants/models';
import AdvancedTableCellRenderer from './AdvancedTableCellRenderer';
import PropTypes from 'prop-types';
import { makeGetFilterModelSelector } from '../../../selectors/models';

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

    this.getTableProps = this.getTableProps.bind(this);
    this.mapColumns = this.mapColumns.bind(this);
    this.mapData = this.mapData.bind(this);
    this.renderCell = this.renderCell.bind(this);
    this.onSetFilter = this.onSetFilter.bind(this);
    this.handleEdit = this.handleEdit.bind(this);
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

  renderCell(props) {
    const { redux } = this.props;
    const propStyles = pick(props, ['width']);

    if (redux) {
      return <ReduxCell {...propStyles} {...props} />;
    }
    return <TableCell {...propStyles} {...props} />;
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
    onSetFilter({ ...this._filter });
    onFetch();
  }

  handleEdit(value, index, id) {
    //TODO something
  }

  mapColumns() {
    const { columns, cells, headers, widgetId, sorting, onSort } = this.props;
    const { resolveProps } = this.context;
    if (columns) {
      return map(columns, (column, index) => {
        return {
          ...column,
          index,
          dataIndex: column.dataIndex || column.id,
          key: column.key || column.id,
          title: resolveProps(column.headerSrc),
          label: column.title,
          cell: resolveProps(column.cellSrc),
          edit: resolveProps(column.editSrc)
        };
      });
    } else {
      return headers.map((header, columnIndex) => {
        const cell = find(cells, c => c.id === header.id);
        return {
          ...header,
          title: this.renderCell({
            ...header,
            key: header.id,
            columnId: header.id,
            widgetId,
            as: 'div',
            sorting: sorting && sorting[header.id],
            onSort
          }),
          dataIndex: header.id,
          columnId: header.id,
          key: header.id,
          render: (value, record, index) => (
            <AdvancedTableCellRenderer
              key={index}
              component={this.renderCell({
                index,
                key: cell.id,
                widgetId,
                columnId: cell.id,
                model: record,
                as: columnIndex === 0 ? 'div' : 'td',
                ...cell
              })}
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
      scroll,
      tableSize,
      className,
      hasFocus,
      useFixedHeader,
      rowColor,
      rowSelection,
      expandable,
      hasSelect,
      onSetSelection,
      isActive,
      filters,
      bordered
    } = this.props;
    return {
      ...this.props,
      className,
      handleEdit: this.handleEdit,
      hasFocus,
      rowColor,
      columns: this.mapColumns(),
      data: this.state.data,
      tableSize,
      rowSelection,
      useFixedHeader,
      scroll,
      expandable,
      hasSelect,
      onSetSelection,
      onFilter: this.onSetFilter,
      isActive,
      filters,
      bordered
    };
  }

  render() {
    return <AdvancedTable {...this.getTableProps()} />;
  }
}

AdvancedTableContainer.contextTypes = {
  resolveProps: PropTypes.func
};

AdvancedTableContainer.defaultProps = {
  filters: {}
};

const mapStateToProps = (state, props) => {
  return {
    filters: makeGetFilterModelSelector(props.widgetId)(state, props)
  };
};

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
          scroll: props.scroll,
          multiHeader: props.multiHeader,
          bordered: props.bordered
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
  }),
  connect(
    mapStateToProps,
    null
  )
)(AdvancedTableContainer);
