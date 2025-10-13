import React, { useContext, useMemo } from 'react'
import { useSelector } from 'react-redux'
import get from 'lodash/get'

import { WidgetHOC } from '../../../core/widget/WidgetHOC'
import StandardWidget from '../StandardWidget'
import Fieldsets from '../Form/fieldsets'
import { N2OPagination } from '../Table/N2OPagination'
import { FactoryContext } from '../../../core/factory/context'
import { WithActiveModel } from '../Widget/WithActiveModel'
import { dataSourceModelByPrefixSelector } from '../../../ducks/datasource/selectors'
import { ModelPrefix } from '../../../core/datasource/const'

import { Tiles } from './Tiles'
import { type TilesModel, type TilesWidgetProps, type StandardWidgetFilter } from './types'

function Widget(props: TilesWidgetProps) {
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
        setPage,
        page,
        loading,
        setResolve,
        count,
    } = props
    const { resolveProps } = useContext(FactoryContext)
    const resolvedFilter = useMemo(() => resolveProps(filter, Fieldsets.StandardFieldset), [filter, resolveProps]) as StandardWidgetFilter
    const place = get(paging, 'place', 'bottomLeft')
    const datasourceModel = useSelector(dataSourceModelByPrefixSelector(datasource, ModelPrefix.source)) as TilesModel[]
    const pagination = {
        [place]: (
            <N2OPagination
                {...paging}
                size={size}
                activePage={page}
                datasource={datasourceModel}
                datasourceId={datasource}
                setPage={setPage}
                count={count}
            />
        ),
    }

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
            <Tiles
                {...props}
                tile={tile}
                tileWidth={width}
                tileHeight={height}
                data={datasourceModel}
                onResolve={setResolve}
                widgetId={widgetId}
            />
        </StandardWidget>
    )
}

Widget.displayName = 'TilesWidgetComponent'

export const TilesWidget = WidgetHOC<TilesWidgetProps>(
    WithActiveModel<TilesWidgetProps>(Widget),
)

TilesWidget.displayName = 'TilesWidget'
