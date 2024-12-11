import React, { useContext, useMemo } from 'react'
import { useSelector } from 'react-redux'

import { WidgetHOC } from '../../../core/widget/WidgetHOC'
import WidgetLayout from '../StandardWidget'
import Fieldsets from '../Form/fieldsets'
import { N2OPagination } from '../Table/N2OPagination'
import { FactoryContext } from '../../../core/factory/context'
import { WithActiveModel } from '../Widget/WithActiveModel'
import { dataSourceModelByPrefixSelector } from '../../../ducks/datasource/selectors'
import { ModelPrefix } from '../../../core/datasource/const'

import { Tiles } from './Tiles'
import { type TilesModel, type TilesWidgetType, type StandardWidgetFilter } from './types'

function TilesWidget(props: TilesWidgetType) {
    const {
        id: widgetId,
        datasource,
        toolbar,
        disabled,
        className,
        style,
        filter,
        tile,
        paging,
        width,
        height,
        size,
        count,
        setPage,
        page,
        loading,
        setResolve,
    } = props
    const { resolveProps } = useContext(FactoryContext)
    const resolvedFilter = useMemo(() => resolveProps(filter, Fieldsets.StandardFieldset), [filter, resolveProps]) as StandardWidgetFilter
    const { place = 'bottomLeft' } = paging
    const datasourceModel = useSelector(dataSourceModelByPrefixSelector(datasource, ModelPrefix.source)) as TilesModel[]
    const pagination = {
        [place]: (
            <N2OPagination
                {...paging}
                size={size}
                count={count}
                activePage={page}
                datasource={datasourceModel}
                setPage={setPage}
            />
        ),
    }

    return (
        <WidgetLayout
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
            <Tiles
                {...props}
                tile={tile}
                tileWidth={width}
                tileHeight={height}
                data={datasourceModel}
                onResolve={setResolve}
                widgetId={widgetId}
            />
        </WidgetLayout>
    )
}

export default WidgetHOC(WithActiveModel(TilesWidget))
