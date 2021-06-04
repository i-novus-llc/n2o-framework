import React from 'react'
import { compose } from 'recompose'
import PropTypes from 'prop-types'

import { dependency } from '../../../core/dependency'
import StandardWidget from '../StandardWidget'
import Fieldsets from '../Form/fieldsets'
import N2OPagination from '../Table/N2OPagination'

import TilesContainer from './TilesContainer'

function TilesWidget(
    {
        id: widgetId,
        toolbar,
        disabled,
        pageId,
        className,
        style,
        filter,
        dataProvider,
        fetchOnInit,
        tile,
        paging,
        colsSm,
        colsMd,
        colsLg,
        width,
        height,
    },
    context,
) {
    const { size } = paging
    const prepareFilters = () => context.resolveProps(filter, Fieldsets.StandardFieldset)

    return (
        <StandardWidget
            disabled={disabled}
            widgetId={widgetId}
            toolbar={toolbar}
            filter={prepareFilters()}
            bottomLeft={paging && paging.place === 'bottomLeft' && <N2OPagination widgetId={widgetId} {...paging} />}
            bottomCenter={paging && paging.place === 'bottomCenter' && <N2OPagination widgetId={widgetId} {...paging} />}
            bottomRight={paging && paging.place === 'bottomRight' && <N2OPagination widgetId={widgetId} {...paging} />}
            topLeft={paging && paging.place === 'topLeft' && <N2OPagination widgetId={widgetId} {...paging} />}
            topCenter={paging && paging.place === 'topCenter' && <N2OPagination widgetId={widgetId} {...paging} />}
            topRight={paging && paging.place === 'topRight' && <N2OPagination widgetId={widgetId} {...paging} />}
            className={className}
            style={style}
        >
            <TilesContainer
                page={1}
                size={size}
                pageId={pageId}
                disabled={disabled}
                dataProvider={dataProvider}
                widgetId={widgetId}
                fetchOnInit={fetchOnInit}
                tile={tile}
                colsSm={colsSm}
                colsMd={colsMd}
                colsLg={colsLg}
                tileWidth={width}
                tileHeight={height}
            />
        </StandardWidget>
    )
}

TilesWidget.propTypes = {
    widgetId: PropTypes.string,
    toolbar: PropTypes.object,
    disabled: PropTypes.bool,
    pageId: PropTypes.string,
    className: PropTypes.string,
    style: PropTypes.object,
    filter: PropTypes.object,
    dataProvider: PropTypes.object,
    fetchOnInit: PropTypes.bool,
    id: PropTypes.string,
    tile: PropTypes.node,
    paging: PropTypes.object,
    colsSm: PropTypes.number,
    colsMd: PropTypes.number,
    colsLg: PropTypes.number,
    width: PropTypes.number,
    height: PropTypes.number,
}

TilesWidget.defaultProps = {
    toolbar: {},
    disabled: false,
    filter: {},
    paging: {
        size: 10,
        prev: true,
        next: true,
    },
}

TilesWidget.contextTypes = {
    resolveProps: PropTypes.func,
}

export default compose(dependency)(TilesWidget)
