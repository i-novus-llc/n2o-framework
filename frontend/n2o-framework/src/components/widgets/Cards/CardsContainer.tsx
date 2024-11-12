import React from 'react'
import { useSelector } from 'react-redux'

import { withWidgetHandlers } from '../hocs/withWidgetHandlers'
import { dataSourceModelByPrefixSelector } from '../../../ducks/datasource/selectors'
import { ModelPrefix } from '../../../core/datasource/const'

import { Cards, type Props as CardsProps } from './Cards'

const CardsContainerBody = ({
    className,
    id,
    cards,
    setResolve,
    dispatch,
    align,
    height,
    datasource,
}: CardsProps & { setResolve(): void }) => {
    const datasourceModel = useSelector(dataSourceModelByPrefixSelector(datasource, ModelPrefix.source)) as Array<{ id: string }>

    return (
        <Cards
            onResolve={setResolve}
            data={datasourceModel}
            className={className}
            id={id}
            cards={cards}
            dispatch={dispatch}
            align={align}
            datasource={datasource}
            height={height}
        />
    )
}

export const CardsContainer = withWidgetHandlers(CardsContainerBody)
export default CardsContainer
