import React from 'react'
import PropTypes from 'prop-types'

import { dependency } from '../../../core/dependency'
import StandardWidget from '../StandardWidget'
import { StandardFieldset } from '../Form/fieldsets'

// eslint-disable-next-line import/no-named-as-default
import CalendarContainer from './CalendarContainer'

function CalendarWidget(props, context) {
    const {
        id: widgetId,
        datasource: modelId = widgetId,
        toolbar,
        disabled,
        pageId,
        className,
        style,
        filter,
        dataProvider,
        fetchOnInit,
        calendar,
        paging,
    } = props
    const { resolveProps } = context
    const { size } = paging

    return (
        <StandardWidget
            disabled={disabled}
            widgetId={widgetId}
            modelId={modelId}
            toolbar={toolbar}
            filter={resolveProps(filter, StandardFieldset)}
            className={className}
            style={style}
        >
            <CalendarContainer
                page={1}
                size={size}
                pageId={pageId}
                disabled={disabled}
                dataProvider={dataProvider}
                widgetId={widgetId}
                modelId={modelId}
                fetchOnInit={fetchOnInit}
                {...calendar}
            />
        </StandardWidget>
    )
}

CalendarWidget.propTypes = {
    datasource: PropTypes.string,
    id: PropTypes.string,
    toolbar: PropTypes.object,
    disabled: PropTypes.bool,
    pageId: PropTypes.string,
    className: PropTypes.string,
    style: PropTypes.object,
    filter: PropTypes.object,
    dataProvider: PropTypes.object,
    fetchOnInit: PropTypes.bool,
    calendar: PropTypes.any,
    paging: PropTypes.object,
}
CalendarWidget.defaultProps = {
    toolbar: {},
    disabled: false,
    filter: {},
    paging: {},
}
CalendarWidget.contextTypes = {
    resolveProps: PropTypes.func,
}

export default dependency(CalendarWidget)
