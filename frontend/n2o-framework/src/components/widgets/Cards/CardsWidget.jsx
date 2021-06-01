import React from 'react'
import PropTypes from 'prop-types'

import { dependency } from '../../../core/dependency'
import StandardWidget from '../StandardWidget'
import { StandardFieldset } from '../Form/fieldsets'
import N2OPagination from '../Table/N2OPagination'

import CardsContainer from './CardsContainer'

function CardsWidget(
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
        paging,
        cards,
        verticalAlign,
        height,
    },
    context,
) {
    const { size } = paging
    const prepareFilters = () => context.resolveProps(filter, StandardFieldset)

    return (
        <StandardWidget
            disabled={disabled}
            widgetId={widgetId}
            toolbar={toolbar}
            filter={prepareFilters()}
            bottomLeft={paging && <N2OPagination {...paging} widgetId={widgetId} />}
            className={className}
            style={style}
        >
            <CardsContainer
                page={1}
                size={size}
                pageId={pageId}
                disabled={disabled}
                dataProvider={dataProvider}
                widgetId={widgetId}
                fetchOnInit={fetchOnInit}
                cards={cards}
                align={verticalAlign}
                height={height}
            />
        </StandardWidget>
    )
}

CardsWidget.propTypes = {
    widgetId: PropTypes.string,
    id: PropTypes.string,
    toolbar: PropTypes.object,
    disabled: PropTypes.bool,
    pageId: PropTypes.string,
    className: PropTypes.string,
    style: PropTypes.object,
    filter: PropTypes.object,
    dataProvider: PropTypes.object,
    fetchOnInit: PropTypes.bool,
    cards: PropTypes.array,
    align: PropTypes.string,
    height: PropTypes.string,
    paging: PropTypes.object,
    verticalAlign: PropTypes.string,
}

CardsWidget.defaultProps = {
    toolbar: {},
    disabled: false,
    filter: {},
    paging: {
        size: 10,
        prev: true,
        next: true,
    },
}

CardsWidget.contextTypes = {
    resolveProps: PropTypes.func,
}

export default dependency(CardsWidget)
