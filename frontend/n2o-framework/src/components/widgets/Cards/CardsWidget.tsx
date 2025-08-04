import React, { useContext, useMemo } from 'react'
import { useSelector } from 'react-redux'
import get from 'lodash/get'

import { WidgetHOC } from '../../../core/widget/WidgetHOC'
import { FactoryContext } from '../../../core/factory/context'
import StandardWidget from '../StandardWidget'
import { StandardFieldset } from '../Form/fieldsets'
import { N2OPagination } from '../Table/N2OPagination'
import { WithActiveModel } from '../Widget/WithActiveModel'
import { dataSourceModelByPrefixSelector } from '../../../ducks/datasource/selectors'
import { DataSourceModels, ModelPrefix } from '../../../core/datasource/const'

import { CardsContainer } from './CardsContainer'
import { type CardsWidgetProps } from './types'

function Widget(props: CardsWidgetProps) {
    const {
        id: widgetId, datasource, toolbar,
        disabled, className, style, filter,
        paging, loading, cards, verticalAlign, height,
        size, count, page, setPage, datasourceModelLength,
    } = props

    const place = get(paging, 'place', 'bottomLeft')
    const { resolveProps } = useContext(FactoryContext)
    const resolvedFilter = useMemo(() => resolveProps(filter as object, StandardFieldset) as CardsWidgetProps['filter'], [filter, resolveProps])
    const dataSourceModel = useSelector(dataSourceModelByPrefixSelector(datasource, ModelPrefix.source)) as DataSourceModels['datasource']

    const pagination = {
        [place]: (
            <N2OPagination
                {...paging}
                size={size}
                count={count}
                activePage={page}
                datasource={dataSourceModel}
                setPage={setPage}
                visible={datasourceModelLength > 0}
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
            <CardsContainer
                {...props}
                cards={cards}
                align={verticalAlign}
                height={height}
                datasourceModelLength={datasourceModelLength}
            />
        </StandardWidget>
    )
}

Widget.displayName = 'CardsWidgetComponent'

export const CardsWidget = WidgetHOC<CardsWidgetProps>(
    WithActiveModel<CardsWidgetProps>(Widget),
)

CardsWidget.displayName = 'CardsWidget'
