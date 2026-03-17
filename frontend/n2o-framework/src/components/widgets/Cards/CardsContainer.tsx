import React from 'react'
import { useSelector } from 'react-redux'

import { withWidgetHandlers } from '../hocs/withWidgetHandlers'
import { getModelByPrefixAndNameSelector } from '../../../ducks/models/selectors'
import { ModelPrefix } from '../../../core/models/types'

import { Cards } from './Cards'
import { type CardsContainerProps } from './types'

const CardsContainerBody = ({
    className,
    id,
    cards,
    setResolve,
    dispatch,
    align,
    height,
    datasource,
}: CardsContainerProps) => {
    const datasourceModel = useSelector(getModelByPrefixAndNameSelector(ModelPrefix.source, datasource)) as Array<{ id: string }>

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
