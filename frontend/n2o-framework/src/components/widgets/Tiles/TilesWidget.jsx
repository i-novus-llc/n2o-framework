import React, { useContext, useMemo } from 'react'
import PropTypes from 'prop-types'
import { useSelector } from 'react-redux'

import { WidgetHOC } from '../../../core/widget/WidgetHOC'
import { widgetPropTypes } from '../../../core/widget/propTypes'
import WidgetLayout from '../StandardWidget'
import Fieldsets from '../Form/fieldsets'
import { N2OPagination } from '../Table/N2OPagination'
import { FactoryContext } from '../../../core/factory/context'
import { WithActiveModel } from '../Widget/WithActiveModel'
import { dataSourceModelByPrefixSelector } from '../../../ducks/datasource/selectors'
import { ModelPrefix } from '../../../core/datasource/const'

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
        size,
        count,
        setPage,
        page,
        loading,
    } = props
    const { resolveProps } = useContext(FactoryContext)
    const resolvedFilter = useMemo(() => resolveProps(filter, Fieldsets.StandardFieldset), [filter, resolveProps])
    const { place = 'bottomLeft' } = paging
    const datasourceModel = useSelector(dataSourceModelByPrefixSelector(datasource, ModelPrefix.source))
    const pagination = {
        [place]: (
            <N2OPagination
                {...paging}
                size={size}
                count={count}
                activePage={page}
                datasource={datasourceModel}
                setPage={setPage}
            />
        ),
    }

    return (
        <WidgetLayout
            disabled={disabled}
            widgetId={widgetId}
            datasource={datasource}
            toolbar={toolbar}
            filter={resolvedFilter}
            {...pagination}
            className={className}
            style={style}
            loading={loading}
        >
            <TilesContainer
                {...props}
                tile={tile}
                tileWidth={width}
                tileHeight={height}
            />
        </WidgetLayout>
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

export default WidgetHOC(WithActiveModel(TilesWidget))
