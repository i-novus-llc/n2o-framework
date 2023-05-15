import React from 'react'
import { compose, mapProps } from 'recompose'
import { useSelector } from 'react-redux'

import { withWidgetHandlers } from '../AdvancedTable/AdvancedTableContainer'
import { dataSourceModelByPrefixSelector } from '../../../ducks/datasource/selectors'
import { ModelPrefix } from '../../../core/datasource/const'

// eslint-disable-next-line import/no-named-as-default
import Tiles from './Tiles'

function TilesContainer(props) {
    // eslint-disable-next-line react/prop-types
    const { datasource } = props
    const datasourceModel = useSelector(dataSourceModelByPrefixSelector(datasource, ModelPrefix.source))

    return <Tiles {...props} data={datasourceModel} />
}

export default compose(
    withWidgetHandlers,
    mapProps(
        ({
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
            datasource,
        }) => ({
            className,
            id: widgetId,
            tile,
            colsSm,
            colsMd,
            colsLg,
            tileWidth,
            tileHeight,
            onResolve: setResolve,
            dispatch,
            datasource,
        }),
    ),
)(TilesContainer)
