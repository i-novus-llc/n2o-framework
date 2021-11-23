import React, { useContext, useMemo } from 'react'
import { omit } from 'lodash'

import { getN2OPagination } from '../Table/N2OPagination'
import WidgetLayout from '../StandardWidget'
import { StandardFieldset } from '../Form/fieldsets'
import { WidgetHOC } from '../../../core/widget/Widget'
import { FactoryContext } from '../../../core/factory/context'

// eslint-disable-next-line import/no-named-as-default
import AdvancedTableContainer from './AdvancedTableContainer'
import { AdvancedTableWidgetTypes } from './propTypes'

const AdvancedTable = (props) => {
    const { id, disabled, toolbar, datasource, className, style, paging, filter, table, setFilter, models } = props
    const { resolveProps } = useContext(FactoryContext)

    const pagination = useMemo(() => {
        const { place = 'bottomLeft' } = paging

        return getN2OPagination(paging, place, id, datasource)
    }, [id, datasource, paging])

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
            setFilter={setFilter}
            filterModel={models.filter}
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

export const AdvancedTableWidget = OmitProps(WidgetHOC(AdvancedTable))
