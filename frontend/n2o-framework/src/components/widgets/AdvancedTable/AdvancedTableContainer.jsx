import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import { compose, withHandlers, getContext } from 'recompose'
import isEqual from 'lodash/isEqual'
import find from 'lodash/find'
import isEmpty from 'lodash/isEmpty'
import pick from 'lodash/pick'
import omit from 'lodash/omit'
import findIndex from 'lodash/findIndex'
import map from 'lodash/map'
import set from 'lodash/set'
import get from 'lodash/get'
import isUndefined from 'lodash/isUndefined'
import { push } from 'connected-react-router'
import { withTranslation } from 'react-i18next'

import columnHOC from '../Table/withColumn'
import TableCell from '../Table/TableCell'
import { getContainerColumns } from '../../../ducks/columns/selectors'
import evalExpression from '../../../utils/evalExpression'
import { dataProviderResolver } from '../../../core/dataProviderResolver'
import { widgetPropTypes } from '../../../core/widget/propTypes'

import AdvancedTableHeaderCell from './AdvancedTableHeaderCell'
// eslint-disable-next-line import/no-named-as-default
import AdvancedTable from './AdvancedTable'
import { WidgetTableTypes } from './propTypes'

const isEqualCollectionItemsById = (data1 = [], data2 = [], selectedId) => {
    const predicate = ({ id }) => id === selectedId

    return isEqual(find(data1, predicate), find(data2, predicate))
}

const ReduxCell = columnHOC(TableCell)

class AdvancedTableContainer extends React.Component {
    constructor(props) {
        super(props)
        const { models } = props
        const { datasource } = models

        this.state = {
            data: this.mapData(datasource),
            columns: this.mapColumns(),
        }

        this.getTableProps = this.getTableProps.bind(this)
        this.mapColumns = this.mapColumns.bind(this)
        this.mapData = this.mapData.bind(this)
        this.renderCell = this.renderCell.bind(this)
        this.handleSetFilter = this.handleSetFilter.bind(this)
    }

    componentDidUpdate(prevProps) {
        const {
            selectedId: prevSelectedId,
            models: prevModels,
            setResolve,
        } = prevProps
        const { hasSelect, models, selectedId, autoFocus, registredColumns } = this.props
        const { datasource } = models
        const { datasource: prevDatasource } = prevModels

        if (!isEqual(prevDatasource, datasource) || !isEqual(prevProps.registredColumns, registredColumns)) {
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
                setResolve(resolveModel)
            }
        }
    }

    componentDidMount() {
        const { models } = this.props
        const { datasource } = models

        if (datasource) {
            this.setState({
                data: this.mapData(datasource),
                columns: this.mapColumns(),
            })
        }
    }

    renderCell(props) {
        const { datasource } = this.props
        const propStyles = pick(props, ['width'])

        return <ReduxCell {...propStyles} {...props} datasource={datasource} />
    }

    handleSetFilter(filterData) {
        const { setFilter, models } = this.props
        const { filter } = models

        const newFilter = {
            ...filter,
            [filterData.id]: filterData.value,
        }

        if (!isEmpty(filterData.value)) {
            newFilter[filterData.id] = filterData.value
        }

        setFilter(newFilter)
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
          id: widgetId,
          sorting,
          setSorting,
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
                  setSorting,
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

  mapData = datasource => datasource?.map(item => ({
      ...item,
      key: item.id,
  }))

  getTableProps() {
      const props = omit(this.props, [
          'cells',
          'headers',
          'datasource',
          'dispatch',
          'onFetch',
          'pageId',
          'sorting',
          'widgetId',
          'models',
      ])
      const { columns, data } = this.state
      const { models } = this.props
      const { filter, multi } = models

      return {
          ...props,
          columns,
          data,
          multi,
          filters: filter,
          onFilter: this.handleSetFilter,
          resolveModel: models.resolve,
      }
  }

  render() {
      return <AdvancedTable {...this.getTableProps()} />
  }
}

AdvancedTableContainer.propTypes = {
    ...widgetPropTypes,
    ...WidgetTableTypes,
    cells: PropTypes.arrayOf(PropTypes.element),
    headers: PropTypes.arrayOf(PropTypes.element),
    registredColumns: PropTypes.any,
    onRowClickAction: PropTypes.func,
}

const mapStateToProps = (state, props) => ({
    registredColumns: getContainerColumns(props.id)(state, props),
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
    withWidgetHandlers,
    connect(
        mapStateToProps,
        null,
    ),
)

export { AdvancedTableContainer }
export default enhance(AdvancedTableContainer)
