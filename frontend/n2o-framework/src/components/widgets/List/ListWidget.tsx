import React, { useContext, useMemo } from 'react'
import { useSelector } from 'react-redux'

import { WidgetHOC } from '../../../core/widget/WidgetHOC'
import { FactoryContext } from '../../../core/factory/context'
import StandardWidget from '../StandardWidget'
import Fieldsets from '../Form/fieldsets'
import { N2OPagination } from '../Table/N2OPagination'
import { WithActiveModel } from '../Widget/WithActiveModel'
import { dataSourceModelByPrefixSelector } from '../../../ducks/datasource/selectors'
import { ModelPrefix } from '../../../core/datasource/const'

import ListContainer from './ListContainer'
import { ListWidgetProps } from './types'

/**
 * Виджет ListWidget
 * @constructor
 */
function ListWidget(props: ListWidgetProps) {
    const {
        id: widgetId,
        datasource,
        toolbar,
        disabled,
        paging,
        className,
        style,
        filter,
        list = {},
        placeholder,
        maxHeight,
        divider,
        rows,
        size,
        count,
        page,
        setPage,
        loading,
        datasourceModelLength,
        rowClick = null,
        hasMoreButton = false,
        hasSelect = false,
        fetchOnScroll = false,
    } = props
    const { place = 'bottomLeft' } = paging
    const datasourceModel = useSelector(dataSourceModelByPrefixSelector(datasource, ModelPrefix.source)) as Array<Record<string, unknown>>

    const pagination = {
        [place]: (
            <N2OPagination
                {...paging}
                size={size}
                count={count}
                activePage={page}
                datasource={datasourceModel}
                setPage={setPage}
                visible={datasourceModelLength > 0}
            />
        ),
    }
    const { resolveProps } = useContext(FactoryContext)
    const resolvedFilter = useMemo(
        () => resolveProps(filter as object, Fieldsets.StandardFieldset) as ListWidgetProps['filter'],
        [filter, resolveProps],
    )
    const resolvedList = useMemo(() => resolveProps(list), [list, resolveProps])

    return (
        <StandardWidget
            disabled={disabled}
            widgetId={widgetId}
            datasource={datasource}
            toolbar={toolbar}
            filter={resolvedFilter}
            pagination={pagination}
            className={className}
            style={style}
            loading={loading}
        >
            <ListContainer
                {...props}
                maxHeight={maxHeight}
                hasMoreButton={hasMoreButton}
                list={resolvedList}
                rowClick={rowClick}
                fetchOnScroll={fetchOnScroll}
                deferredSpinnerStart={0}
                divider={divider}
                hasSelect={hasSelect}
                placeholder={placeholder}
                rows={rows}
                datasourceModelLength={datasourceModelLength}
            />
        </StandardWidget>
    )
}

export default WidgetHOC(WithActiveModel(ListWidget))
