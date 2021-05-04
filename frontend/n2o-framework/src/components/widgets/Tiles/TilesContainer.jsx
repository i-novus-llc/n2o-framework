import React from 'react'
import { compose, mapProps } from 'recompose'

import { withWidgetHandlers } from '../AdvancedTable/AdvancedTableContainer'
import { withContainerLiveCycle } from '../Table/TableContainer'
import widgetContainer from '../WidgetContainer'

import Tiles from './Tiles'

function TilesContainer(props) {
    return <Tiles {...props} />
}

export default compose(
    widgetContainer(
        {
            mapProps: props => ({
                ...props,
            }),
        },
        'TilesWidget',
    ),
    withContainerLiveCycle,
    withWidgetHandlers,
    mapProps(
        ({
            datasource,
            className,
            widgetId,
            tile,
            colsSm,
            colsMd,
            colsLg,
            tileWidth,
            tileHeight,
            onResolve,
            dispatch,
        }) => ({
            className,
            id: widgetId,
            tile,
            data: datasource,
            colsSm,
            colsMd,
            colsLg,
            tileWidth,
            tileHeight,
            onResolve,
            dispatch,
        }),
    ),
)(TilesContainer)
