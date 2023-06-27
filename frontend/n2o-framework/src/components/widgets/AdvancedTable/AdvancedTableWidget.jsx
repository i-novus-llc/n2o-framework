import React, { useContext, useMemo } from 'react'
import omit from 'lodash/omit'
import defaultTo from 'lodash/defaultTo'
import get from 'lodash/get'
import { useSelector, connect } from 'react-redux'
import { compose } from 'recompose'

import { N2OPagination } from '../Table/N2OPagination'
import WidgetLayout from '../StandardWidget'
import { StandardFieldset } from '../Form/fieldsets'
import { WidgetHOC } from '../../../core/widget/WidgetHOC'
import { FactoryContext } from '../../../core/factory/context'
import { WithActiveModel } from '../Widget/WithActiveModel'
import { dataSourceModelByPrefixSelector } from '../../../ducks/datasource/selectors'
import { ModelPrefix } from '../../../core/datasource/const'
import { getContainerColumns } from '../../../ducks/columns/selectors'

// eslint-disable-next-line import/no-named-as-default
import AdvancedTableContainer from './AdvancedTableContainer'
import { AdvancedTableWidgetTypes } from './propTypes'

const AdvancedTable = (props) => {
    const {
        id, disabled, toolbar, datasource, className, setPage, loading, fetchData,
        style, paging, filter, table, size, count, page, hasNext, getColumnsVisibility,
    } = props
    const datasourceModel = useSelector(dataSourceModelByPrefixSelector(datasource, ModelPrefix.source))

    const { resolveProps } = useContext(FactoryContext)
    const { place = 'bottomLeft' } = paging

    const paginationVisible = getColumnsVisibility()

    const pagination = {
        [place]: (
            <N2OPagination
                {...paging}
                size={size}
                count={count}
                activePage={page}
                datasource={datasourceModel}
                setPage={setPage}
                hasNext={hasNext}
                loading={loading}
                visible={paginationVisible}
            />
        ),
    }

    const resolvedFilter = useMemo(() => resolveProps(filter, StandardFieldset), [filter, resolveProps])
    const resolvedTable = useMemo(() => ({
        ...table,
        cells: table.cells.map(cell => resolveProps(cell)),
        headers: table.headers.map(header => resolveProps(header)),
    }), [table, resolveProps])

    return (
        <WidgetLayout
            disabled={disabled}
            widgetId={id}
            datasource={datasource}
            toolbar={toolbar}
            filter={resolvedFilter}
            className={className}
            style={style}
            fetchData={fetchData}
            loading={loading}
            {...pagination}
        >
            <AdvancedTableContainer
                {...props}
                {...resolvedTable}
            />
        </WidgetLayout>
    )
}

AdvancedTable.propTypes = AdvancedTableWidgetTypes

// FIXME удалить костыль
const OmitProps = Component => (props) => {
    const omited = omit(props, [])

    omited.table = omit(omited.table, ['sorting', 'size'])

    return (
        <Component {...omited} />
    )
}

const mapStateToProps = (state, props) => ({
    getColumnsVisibility: () => {
        const { id } = props
        const columns = getContainerColumns(id)(state)
        const columnsKeys = Object.keys(columns)

        return columnsKeys.some(columnKey => get(columns, `${columnKey}.visible`))
    },
})

const Enhanced = WithActiveModel(
    AdvancedTable,
    props => props.table?.hasSelect && defaultTo(props.table?.autoSelect, true),
)

export const AdvancedTableWidget = compose(
    OmitProps,
    WidgetHOC,
    connect(mapStateToProps),
)(Enhanced)
