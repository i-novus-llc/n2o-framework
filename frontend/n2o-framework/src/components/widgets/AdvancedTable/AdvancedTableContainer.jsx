import React from 'react';
import { connect } from 'react-redux';
import { compose, withHandlers } from 'recompose';
import isEqual from 'lodash/isEqual';
import find from 'lodash/find';
import isEmpty from 'lodash/isEmpty';
import pick from 'lodash/pick';
import forOwn from 'lodash/forOwn';
import omit from 'lodash/omit';
import findIndex from 'lodash/findIndex';
import map from 'lodash/map';
import set from 'lodash/set';
import AdvancedTable from './AdvancedTable';
import widgetContainer from '../WidgetContainer';
import { setTableSelectedId } from '../../../actions/widgets';
import { TABLE } from '../widgetTypes';
import columnHOC from '../Table/withColumn';
import TableCell from '../Table/TableCell';
import { setModel } from '../../../actions/models';
import { PREFIXES } from '../../../constants/models';
import PropTypes from 'prop-types';
import { makeGetFilterModelSelector } from '../../../selectors/models';
import { getContainerColumns } from '../../../selectors/columns';
import evalExpression from '../../../utils/evalExpression';

const isEqualCollectionItemsById = (data1 = [], data2 = [], selectedId) => {
  const predicate = ({ id }) => id == selectedId;
  return isEqual(find(data1, predicate), find(data2, predicate));
};

const ReduxCell = columnHOC(TableCell);

class AdvancedTableContainer extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      data: this.mapData(props.datasource),
      columns: this.mapColumns(),
    };

    this._filter = props.filters;

    this.getTableProps = this.getTableProps.bind(this);
    this.mapColumns = this.mapColumns.bind(this);
    this.mapData = this.mapData.bind(this);
    this.renderCell = this.renderCell.bind(this);
    this.handleSetFilter = this.handleSetFilter.bind(this);
    this.onEdit = this.onEdit.bind(this);
  }

  componentDidUpdate(prevProps, prevState) {
    const {
      selectedId: prevSelectedId,
      datasource: prevDatasource,
      onResolve,
    } = prevProps;
    const { hasSelect, datasource, selectedId } = this.props;

    if (!isEqual(prevProps.datasource, this.props.datasource)) {
      this.setState({
        data: this.mapData(this.props.datasource),
        columns: this.mapColumns(),
      });
    }

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

  componentDidMount(prevProps) {
    if (this.props.datasource) {
      this.setState({
        data: this.mapData(this.props.datasource),
        columns: this.mapColumns(),
      });
    }
  }

  renderCell(props) {
    const { actions } = this.props;
    const propStyles = pick(props, ['width']);
    return <ReduxCell {...propStyles} {...props} actions={actions} />;
  }

  handleSetFilter(filter) {
    const { onSetFilter, onFetch } = this.props;
    this._filter = {
      ...this._filter,
      [filter.id]: filter.value,
    };
    forOwn(this._filter, (v, k) => {
      if (!v || isEmpty(v)) delete this._filter[k];
    });
    onSetFilter({ ...this._filter });
    onFetch();
  }

  onEdit(value, index, id) {
    //TODO something
  }

  mapColumns() {
    const {
      cells,
      headers,
      widgetId,
      sorting,
      onSort,
      registredColumns,
    } = this.props;

    map(registredColumns, ({ frozen, visible }, key) => {
      if (frozen && !visible) {
        const headerIndex = findIndex(headers, ({ id }) => id === key);

        set(headers, `[${headerIndex}].needRender`, false);
      }
    });

    return headers.map(header => {
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
          onSort,
        }),
        label: header.title,
        dataIndex: header.id,
        columnId: header.id,
        key: header.id,
        hasSpan: cell.hasSpan,
        render: (value, record, index) => ({
          needRender: header.needRender,
          children: this.renderCell({
            index,
            key: cell.id,
            widgetId,
            columnId: cell.id,
            model: record,
            as: 'div',
            needRender: header.needRender,
            ...cell,
          }),
        }),
      };
    });
  }

  mapData(datasource) {
    if (!datasource) return;
    return datasource.map(item => {
      return {
        ...item,
        key: item.id,
      };
    });
  }

  getTableProps() {
    const props = omit(this.props, [
      'actions',
      'cells',
      'headers',
      'datasource',
      'dispatch',
      'onActionImpl',
      'onEdit',
      'onFetch',
      'pageId',
      'redux',
      'sorting',
      'widgetId',
    ]);

    return {
      ...props,
      onEdit: this.onEdit,
      columns: this.state.columns,
      data: this.state.data,
      onFilter: this.handleSetFilter,
    };
  }

  render() {
    return <AdvancedTable {...this.getTableProps()} />;
  }
}

AdvancedTableContainer.contextTypes = {
  resolveProps: PropTypes.func,
  expandedFieldId: PropTypes.string,
};

AdvancedTableContainer.defaultProps = {
  filters: {},
};

const mapStateToProps = (state, props) => {
  return {
    filters: makeGetFilterModelSelector(props.widgetId)(state, props),
    registredColumns: getContainerColumns(props.widgetId)(state, props),
  };
};

const withWidgetHandlers = withHandlers({
  onRowClickAction: ({ rowClick, onActionImpl }) => model => {
    const { enablingCondition } = rowClick;
    const allowRowClick = evalExpression(enablingCondition, model);
    (allowRowClick && onActionImpl(rowClick)) ||
      (allowRowClick === undefined && onActionImpl(rowClick));
  },
});

const enhance = compose(
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
          rowClass: props.rowClass,
          onFetch: props.onFetch,
          onSort: props.onSort,
          onResolve: newModel => {
            props.onResolve(newModel);
            if (props.selectedId != newModel.id) {
              props.dispatch(setTableSelectedId(props.widgetId, newModel.id));
            }
          },
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
          placeholder: props.placeholder,
          useFixedHeader: props.useFixedHeader,
          expandable: props.expandable,
          scroll: props.scroll,
          multiHeader: props.multiHeader,
          bordered: props.bordered,
          rowClick: props.rowClick,
          onActionImpl: props.onActionImpl,
          expandedFieldId: props.expandedFieldId,
          className: props.className,
          rows: props.rows,
        };
      },
    },
    TABLE
  ),
  withWidgetHandlers,
  connect(
    mapStateToProps,
    null
  )
);
export { AdvancedTableContainer };
export default enhance(AdvancedTableContainer);
