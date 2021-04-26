import React from 'react'
import { compose } from 'recompose'
import PropTypes from 'prop-types'

import dependency from '../../../core/dependency'
import StandardWidget from '../StandardWidget'
import Fieldsets from '../Form/fieldsets'
import Pagination from '../Table/TablePagination'

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
        prevText,
        nextText,
        paging,
        cards,
        verticalAlign,
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
            bottomLeft={paging && <Pagination {...paging} widgetId={widgetId} />}
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

export default compose(dependency)(CardsWidget)
