import React, { useCallback } from 'react'
import PropTypes from 'prop-types'
import { connect, useStore, useDispatch } from 'react-redux'
import { compose } from 'recompose'
import isEqual from 'lodash/isEqual'
import find from 'lodash/find'
import isEmpty from 'lodash/isEmpty'
import isNumber from 'lodash/isNumber'
import pick from 'lodash/pick'
import omit from 'lodash/omit'
import findIndex from 'lodash/findIndex'
import map from 'lodash/map'
import set from 'lodash/set'
import get from 'lodash/get'
import isUndefined from 'lodash/isUndefined'
import merge from 'deepmerge'
import { push } from 'connected-react-router'
import { withTranslation } from 'react-i18next'

import { withSecurityList } from '../../../core/auth/withSecurity'
import columnHOC from '../Table/withColumn'
import TableCell from '../Table/TableCell'
import { getContainerColumns } from '../../../ducks/columns/selectors'
import evalExpression from '../../../utils/evalExpression'
import { dataProviderResolver } from '../../../core/dataProviderResolver'
import { widgetPropTypes } from '../../../core/widget/propTypes'
import { ModelPrefix, SortDirection } from '../../../core/datasource/const'

import AdvancedTableHeaderCell from './AdvancedTableHeaderCell'
// eslint-disable-next-line import/no-named-as-default
import AdvancedTable from './AdvancedTable'
import { WidgetTableTypes } from './propTypes'

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
            models: prevModels,
            sorting: prevSorting,
        } = prevProps
        const { models, registredColumns, sorting, headers } = this.props
        const { datasource } = models
        const { datasource: prevDatasource } = prevModels

        if (
            !isEqual(prevDatasource, datasource) ||
            !isEqual(prevProps.registredColumns, registredColumns) ||
            !isEqual(sorting, prevSorting) ||
            !isEqual(prevProps.headers, headers)
        ) {
            this.setState({
                data: this.mapData(datasource),
                columns: this.mapColumns(),
            })
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
        const { setFilter, models, fetchData } = this.props
        const { filter } = models

        const newFilter = {
            ...filter,
            [filterData.id]: filterData.value,
        }

        if (isNumber(filterData.value) || !isEmpty(filterData.value)) {
            newFilter[filterData.id] = filterData.value
        }

        setFilter(newFilter)
        fetchData()
    }

    mapHeaders = (headers, isChild = false) => map(headers, (header) => {
        let mappedChildren = null

        if (header.children || isChild) {
            const { multiHeader = false, visible = true } = header
            const needHideChildren = multiHeader && !visible
            const finalHeader = needHideChildren
                ? header.children.map(child => ({ ...child, visible: false }))
                : header.children

            mappedChildren = this.mapHeaders(finalHeader || [], true)

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
            headers: propsHeaders,
            id: widgetId,
            sorting,
            setSorting,
            registredColumns,
            filters,
        } = this.props

        const headers = map(propsHeaders, (header) => {
            const { multiHeader, visible, children } = header

            if (multiHeader && visible === false && !isEmpty(children)) {
                const newChildren = children.map(child => ({ ...child, visible: false }))

                return { ...header, children: newChildren }
            }

            return header
        })

        map(registredColumns, ({ frozen, visible }, key) => {
            if (frozen && !visible) {
                const headerIndex = findIndex(headers, ({ id }) => id === key)

                set(headers, `[${headerIndex}].needRender`, false)
            }
        })

        return this.mapHeaders(headers).map((header) => {
            const { visible, multiHeader } = header

            const cell = find(cells, c => c.id === header.id) || {}
            const children = get(header, 'children', null)
            const needRender = visible === undefined ? get(registredColumns, `${header.id}.visible`, true) : visible

            const mapChildren = children => map(children, (child) => {
                if (multiHeader && !needRender) {
                    return []
                }

                if (!isEmpty(child.children)) {
                    child.children = mapChildren(child.children)
                }

                const cell = find(cells, c => c.id === child.id) || {}
                const compiledSorting = isEmpty(sorting) ? SortDirection.none : sorting[child.sortingParam]

                return {
                    ...child,
                    title: (
                        <AdvancedTableHeaderCell
                            as="div"
                            {...child}
                            onFilter={this.handleSetFilter}
                            filters={filters}
                            filterControl={child.filterControl}
                            setSorting={setSorting}
                            sorting={compiledSorting}
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
                    sorting: sorting?.[header.sortingParam],
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

    mapChildren = (children, parentId) => children.map(child => ({ ...child, key: `${parentId}_${child.id}` }))

    mapData = datasource => datasource?.map((item = {}) => {
        const { children, id } = item

        if (children) {
            return {
                ...item,
                key: id,
                children: this.mapChildren(children, id),
            }
        }

        return {
            ...item,
            key: id,
        }
    })

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

export const withWidgetHandlers = (WrappedComponent) => {
    const WithHandlers = (props) => {
        const { rowClick, datasource } = props
        const store = useStore()
        const dispatch = useDispatch()
        const state = store.getState()
        const onRowClickAction = useCallback((model) => {
            const {
                enablingCondition,
                action,
                url,
                pathMapping,
                queryMapping,
                target,
            } = rowClick
            const updatedState = !isEmpty(model) ? merge(state, {
                models: {
                    [ModelPrefix.active]: {
                        [datasource]: model,
                    },
                },
            }) : state

            const allowRowClick = evalExpression(enablingCondition, model)
            const { url: compiledUrl } = dataProviderResolver(updatedState, {
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
        }, [dispatch, datasource, rowClick, state])

        return (
            <WrappedComponent {...props} onRowClickAction={onRowClickAction} />
        )
    }

    WithHandlers.propTypes = {
        rowClick: PropTypes.object,
        datasource: PropTypes.string,
    }

    return WithHandlers
}

const enhance = compose(
    withTranslation(),
    withWidgetHandlers,
    connect(
        mapStateToProps,
        null,
    ),
)

export { AdvancedTableContainer }
const enhanced = enhance(AdvancedTableContainer)

export default withSecurityList(enhanced, 'headers')
