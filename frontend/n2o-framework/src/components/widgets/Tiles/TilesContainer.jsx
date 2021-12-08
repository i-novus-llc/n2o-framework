import React from 'react'
import { compose, mapProps } from 'recompose'

import { withWidgetHandlers } from '../AdvancedTable/AdvancedTableContainer'

// eslint-disable-next-line import/no-named-as-default
import Tiles from './Tiles'

function TilesContainer(props) {
    return <Tiles {...props} />
}

export default compose(
    withWidgetHandlers,
    mapProps(
        ({
            models,
            className,
            widgetId,
            tile,
            colsSm,
            colsMd,
            colsLg,
            tileWidth,
            tileHeight,
            setResolve,
            dispatch,
        }) => ({
            className,
            id: widgetId,
            tile,
            data: models.datasource,
            colsSm,
            colsMd,
            colsLg,
            tileWidth,
            tileHeight,
            onResolve: setResolve,
            dispatch,
        }),
    ),
)(TilesContainer)
