import React, { useContext, useMemo } from 'react'
import PropTypes from 'prop-types'

import { WidgetHOC } from '../../../core/widget/Widget'
import { widgetPropTypes } from '../../../core/widget/propTypes'
import StandardWidget from '../StandardWidget'
import Fieldsets from '../Form/fieldsets'
import { getN2OPagination } from '../Table/N2OPagination'
import { FactoryContext } from '../../../core/factory/context'

import TilesContainer from './TilesContainer'

function TilesWidget(props) {
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
    } = props
    const { resolveProps } = useContext(FactoryContext)
    const resolvedFilter = useMemo(() => resolveProps(filter, Fieldsets.StandardFieldset), [filter, resolveProps])
    const { place = 'bottomLeft' } = paging

    return (
        <StandardWidget
            disabled={disabled}
            widgetId={widgetId}
            datasource={datasource}
            toolbar={toolbar}
            filter={resolvedFilter}
            {...getN2OPagination(paging, place, widgetId, datasource)}
            className={className}
            style={style}
        >
            <TilesContainer
                {...props}
                tile={tile}
                tileWidth={width}
                tileHeight={height}
            />
        </StandardWidget>
    )
}

TilesWidget.propTypes = {
    ...widgetPropTypes,
    tile: PropTypes.node,
    colsSm: PropTypes.number,
    colsMd: PropTypes.number,
    colsLg: PropTypes.number,
    width: PropTypes.number,
    height: PropTypes.number,
}
const Widget = WidgetHOC(TilesWidget)

Widget.defaultProps = {
    fetchOnInit: true,
}

export default Widget
