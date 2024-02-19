import React, { useContext, useMemo } from 'react'
import PropTypes from 'prop-types'
import { useSelector } from 'react-redux'

import { WidgetHOC } from '../../../core/widget/WidgetHOC'
import { widgetPropTypes } from '../../../core/widget/propTypes'
import { FactoryContext } from '../../../core/factory/context'
import WidgetLayout from '../StandardWidget'
import { StandardFieldset } from '../Form/fieldsets'
import { N2OPagination } from '../Table/N2OPagination'
import { WithActiveModel } from '../Widget/WithActiveModel'
import { dataSourceModelByPrefixSelector } from '../../../ducks/datasource/selectors'
import { ModelPrefix } from '../../../core/datasource/const'

import CardsContainer from './CardsContainer'

function CardsWidget(props) {
    const {
        id: widgetId,
        datasource,
        toolbar, disabled, className,
        style, filter, paging, loading,
        cards, verticalAlign, height,
        size, count, page, setPage,
    } = props
    const { place = 'bottomLeft' } = paging
    const { resolveProps } = useContext(FactoryContext)
    const resolvedFilter = useMemo(() => resolveProps(filter, StandardFieldset), [filter, resolveProps])
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
            />
        </WidgetLayout>
    )
}

CardsWidget.propTypes = {
    ...widgetPropTypes,
    cards: PropTypes.array,
    align: PropTypes.string,
    height: PropTypes.string,
    verticalAlign: PropTypes.string,
}

/**
 * @type ConnectedWidget
 */
export default WidgetHOC(WithActiveModel(CardsWidget))
