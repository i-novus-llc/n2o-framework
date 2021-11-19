import React from 'react'
import { compose, mapProps } from 'recompose'

import { withWidgetHandlers } from '../AdvancedTable/AdvancedTableContainer'
import { withContainerLiveCycle } from '../Table/withContainerLiveCycle'
import widgetContainer from '../WidgetContainer'

import { Cards } from './Cards'

const CardsContainer = props => <Cards {...props} />

export default compose(
    widgetContainer({
        mapProps: props => ({
            ...props,
        }),
    }),
    withContainerLiveCycle,
    withWidgetHandlers,
    mapProps(
        ({
            datasource,
            className,
            widgetId,
            cards,
            onResolve,
            dispatch,
            align,
            height,
        }) => ({
            className,
            id: widgetId,
            cards,
            data: datasource,
            onResolve,
            dispatch,
            align,
            height,
        }),
    ),
)(CardsContainer)
