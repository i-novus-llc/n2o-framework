import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import { compose, withHandlers, getContext } from 'recompose'
import isEqual from 'lodash/isEqual'
import find from 'lodash/find'
import isEmpty from 'lodash/isEmpty'
import pick from 'lodash/pick'
import forOwn from 'lodash/forOwn'
import omit from 'lodash/omit'
import findIndex from 'lodash/findIndex'
import map from 'lodash/map'
import set from 'lodash/set'
import get from 'lodash/get'
import isUndefined from 'lodash/isUndefined'
import { push } from 'connected-react-router'
import { withTranslation } from 'react-i18next'

import widgetContainer from '../WidgetContainer'
import { setTableSelectedId } from '../../../ducks/widgets/store'
import { TABLE } from '../widgetTypes'
import columnHOC from '../Table/withColumn'
import TableCell from '../Table/TableCell'
import { setModel } from '../../../ducks/models/store'
import { PREFIXES } from '../../../ducks/models/constants'
import {
    makeGetFilterModelSelector,
    makeGetModelByPrefixSelector,
} from '../../../ducks/models/selectors'
import { getContainerColumns } from '../../../ducks/columns/selectors'
import evalExpression from '../../../utils/evalExpression'
import { dataProviderResolver } from '../../../core/dataProviderResolver'

import AdvancedTableHeaderCell from './AdvancedTableHeaderCell'
// eslint-disable-next-line import/no-named-as-default
import AdvancedTable from './AdvancedTable'

const isEqualCollectionItemsById = (data1 = [], data2 = [], selectedId) => {
    const predicate = ({ id }) => id === selectedId

    return isEqual(find(data1, predicate), find(data2, predicate))
}

const ReduxCell = columnHOC(TableCell)

class AdvancedTableContainer extends React.Component {
    constructor(props) {
        super(props)

        this.state = {
            data: this.mapData(props.datasource),
            columns: this.mapColumns(),
        }

        this.filterValue = props.filters

        this.getTableProps = this.getTableProps.bind(this)
        this.mapColumns = this.mapColumns.bind(this)
        this.mapData = this.mapData.bind(this)
        this.renderCell = this.renderCell.bind(this)
        this.handleSetFilter = this.handleSetFilter.bind(this)
    }

    componentDidUpdate(prevProps) {
        const {
            selectedId: prevSelectedId,
            datasource: prevDatasource,
            onResolve,
        } = prevProps
        const { hasSelect, datasource, selectedId, autoFocus, registredColumns } = this.props

        if (!isEqual(prevProps.datasource, datasource) || !isEqual(prevProps.registredColumns, registredColumns)) {
            this.setState({
                data: this.mapData(datasource),
                columns: this.mapColumns(),
            })
        }

        if (
            hasSelect &&
            !isEmpty(datasource) &&
            !isEqual(prevDatasource, datasource) &&
            (
                !selectedId ||
                !isEqual(prevSelectedId, selectedId) ||
                !isEqualCollectionItemsById(prevDatasource, datasource, selectedId)
            )
        ) {
            const selectedModel = find(datasource, model => model.id === selectedId)

            const resolveModel = autoFocus
                ? selectedModel || datasource[0]
                : selectedModel || {}

            if (!isEmpty(resolveModel)) {
                onResolve(resolveModel)
            }
        }
    }

    componentDidMount() {
        const { datasource } = this.props

        if (datasource) {
            this.setState({
                data: this.mapData(datasource),
                columns: this.mapColumns(),
            })
        }
    }

    renderCell(props) {
        const { actions, modelId } = this.props
        const propStyles = pick(props, ['width'])

        return <ReduxCell {...propStyles} {...props} actions={actions} modelId={modelId} />
    }

    handleSetFilter(filter) {
        const { onSetFilter, onFetch } = this.props

        this.filterValue = {
            ...this.filterValue,
            [filter.id]: filter.value,
        }
        forOwn(this.filterValue, (v, k) => {
            if (!v || isEmpty(v)) { delete this.filterValue[k] }
        })
        onSetFilter({ ...this.filterValue })
        onFetch()
    }

  mapHeaders = (headers, isChild = false) => map(headers, (header) => {
      let mappedChildren = null

      if (header.children || isChild) {
          mappedChildren = this.mapHeaders(header.children || [], true)

          return {
              ...header,
              dataIndex: header.id,
              title: header.label,
              children: header.children ? mappedChildren : undefined,
          }
      }

      return header
  });

  mapColumns() {
      const {
          cells,
          headers,
          widgetId,
          modelId,
          sorting,
          onSort,
          registredColumns,
          filters,
      } = this.props

      map(registredColumns, ({ frozen, visible }, key) => {
          if (frozen && !visible) {
              const headerIndex = findIndex(headers, ({ id }) => id === key)

              set(headers, `[${headerIndex}].needRender`, false)
          }
      })

      return this.mapHeaders(headers).map((header) => {
          const cell = find(cells, c => c.id === header.id) || {}
          const children = get(header, 'children', null)
          const needRender = get(registredColumns, `${header.id}.visible`, true)

          const mapChildren = children => map(children, (child) => {
              if (!isEmpty(child.children)) {
                  child.children = mapChildren(child.children)
              }

              const cell = find(cells, c => c.id === child.id) || {}

              return {
                  ...child,
                  title: (
                      <AdvancedTableHeaderCell
                          as="div"
                          {...child}
                          onFilter={this.handleSetFilter}
                          filters={filters}
                          filterControl={child.filterControl}
                      />
                  ),
                  render: (value, record, index) => ({
                      children: this.renderCell({
                          index,
                          key: cell.id,
                          widgetId,
                          modelId,
                          columnId: cell.id,
                          model: record,
                          as: 'div',
                          needRender,
                          ...cell,
                      }),
                  }),
              }
          })

          if (children) {
              header = { ...header }
              header.children = mapChildren(children)
          }

          return {
              ...header,
              needRender,
              title: this.renderCell({
                  ...header,
                  key: header.id,
                  columnId: header.id,
                  widgetId,
                  as: 'div',
                  sorting: sorting && sorting[header.id],
                  needRender,
                  onSort,
              }),
              label: header.title,
              dataIndex: header.id,
              columnId: header.id,
              key: header.id,
              hasSpan: get(cell, 'hasSpan', false),
              render: (value, record, index) => ({
                  needRender: header.needRender,
                  children: this.renderCell({
                      index,
                      key: cell.id,
                      widgetId,
                      columnId: cell.id,
                      model: record,
                      as: 'div',
                      needRender,
                      ...cell,
                  }),
              }),
          }
      })
  }

  mapData = (datasource) => {
      if (!datasource) { return }

      // eslint-disable-next-line consistent-return
      return map(datasource, item => ({
          ...item,
          key: item.id,
      }))
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
      ])
      const { columns, data } = this.state

      return {
          ...props,
          onEdit: () => {},
          columns,
          data,
          onFilter: this.handleSetFilter,
      }
  }

  render() {
      return <AdvancedTable {...this.getTableProps()} />
  }
}

AdvancedTableContainer.propTypes = {
    autoFocus: PropTypes.any,
    selectedId: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    onResolve: PropTypes.func,
    hasSelect: PropTypes.bool,
    actions: PropTypes.any,
    cells: PropTypes.any,
    datasource: PropTypes.any,
    headers: PropTypes.any,
    widgetId: PropTypes.string,
    modelId: PropTypes.string,
    sorting: PropTypes.any,
    onSort: PropTypes.func,
    onFetch: PropTypes.func,
    onSetFilter: PropTypes.func,
    registredColumns: PropTypes.any,
    filters: PropTypes.object,
}

AdvancedTableContainer.contextTypes = {
    resolveProps: PropTypes.func,
    expandedFieldId: PropTypes.string,
}

AdvancedTableContainer.defaultProps = {
    filters: {},
}

const mapStateToProps = (state, props) => ({
    filters: makeGetFilterModelSelector(props.widgetId)(state, props),
    registredColumns: getContainerColumns(props.widgetId)(state, props),
    multi: makeGetModelByPrefixSelector(PREFIXES.multi, props.widgetId)(
        state,
        props,
    ),
})

export const withWidgetHandlers = compose(
    getContext({
        store: PropTypes.object,
    }),
    withHandlers({
        onRowClickAction: ({ rowClick, dispatch, store }) => (model) => {
            const state = store.getState()
            const {
                enablingCondition,
                action,
                url,
                pathMapping,
                queryMapping,
                target,
            } = rowClick
            const allowRowClick = evalExpression(enablingCondition, model)
            const { url: compiledUrl } = dataProviderResolver(state, {
                url,
                pathMapping,
                queryMapping,
            })

            if (action && (allowRowClick || isUndefined(allowRowClick))) {
                dispatch(action)
            } else if (url) {
                if (target === 'application') {
                    dispatch(push(compiledUrl))
                } else if (target === '_blank') {
                    window.open(compiledUrl)
                } else {
                    window.location = compiledUrl
                }
            }
        },
    }),
)

const enhance = compose(
    withTranslation(),
    widgetContainer(
        {
            mapProps: props => ({
                widgetId: props.widgetId,
                modelId: props.modelId,
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
                children: props.children,
                onResolve: (newModel) => {
                    props.onResolve(newModel)
                    if (props.selectedId !== newModel.id) {
                        props.dispatch(setTableSelectedId(props.widgetId, newModel.id))
                    }
                },
                onSetSelection: (models) => {
                    props.dispatch(setModel(PREFIXES.multi, props.modelId, models))
                },
                setSelectionType: (type) => {
                    props.dispatch(
                        setModel(PREFIXES.selectionType, props.modelId, type),
                    )
                },
                onSetFilter: (filters) => {
                    props.dispatch(setModel(PREFIXES.filter, props.modelId, filters))
                },
                onFocus: props.onFocus,
                size: props.size,
                actions: props.actions,
                redux: true,
                rowSelection: props.rowSelection,
                autoCheckboxOnSelect: props.autoCheckboxOnSelect,
                tableSize: props.tableSize,
                placeholder: props.placeholder,
                useFixedHeader: props.useFixedHeader,
                expandable: props.expandable,
                scroll: props.scroll,
                multiHeader: props.multiHeader,
                bordered: props.bordered,
                rowClick: props.rowClick,
                expandedFieldId: props.expandedFieldId,
                className: props.className,
                rows: props.rows,
                dispatch: props.dispatch,
                width: props.width,
                height: props.height,
                textWrap: props.textWrap,
                t: props.t,
                resolveModel: props.resolveModel,
            }),
        },
        TABLE,
    ),
    withWidgetHandlers,
    connect(
        mapStateToProps,
        null,
    ),
)

export { AdvancedTableContainer }
export default enhance(AdvancedTableContainer)
