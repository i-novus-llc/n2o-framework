import React from 'react'
import PropTypes from 'prop-types'

import { dependency } from '../../../core/dependency'
import StandardWidget from '../StandardWidget'
import { StandardFieldset } from '../Form/fieldsets'

import ChartContainer from './ChartWidgetContainer'

/**
 * Виджет графиков
 * @param widgetId
 * @param toolbar
 * @param disabled
 * @param actions
 * @param chart
 * @param pageId
 * @param filter
 * @param className
 * @param style
 * @param rest
 * @param resolveProps
 * @return {*}
 * @constructor
 */
function ChartWidget(
    {
        id: widgetId,
        datasource: modelId = widgetId,
        toolbar,
        disabled,
        actions,
        chart,
        pageId,
        filter,
        className,
        style,
        ...rest
    },
    { resolveProps },
) {
    const prepareFilters = () => resolveProps(filter, StandardFieldset)
    const getWidgetProps = () => ({
        widgetId,
        toolbar,
        disabled,
        actions,
        pageId,
        chart,
        ...rest,
    })

    const { fetchOnInit } = chart

    return (
        <StandardWidget
            disabled={disabled}
            widgetId={widgetId}
            modelId={modelId}
            toolbar={toolbar}
            actions={actions}
            filter={prepareFilters()}
            className={className}
            style={style}
        >
            <ChartContainer
                widgetId={widgetId}
                modelId={modelId}
                pageId={pageId}
                fetchOnInit={fetchOnInit}
                {...getWidgetProps()}
            />
        </StandardWidget>
    )
}

ChartWidget.propTypes = {
    disabled: PropTypes.bool,
    id: PropTypes.string,
    datasource: PropTypes.string,
    className: PropTypes.string,
    style: PropTypes.object,
    filter: PropTypes.object,
    pageId: PropTypes.string.isRequired,
    widgetId: PropTypes.string,
    actions: PropTypes.object,
    toolbar: PropTypes.object,
    dataProvider: PropTypes.object,
    chart: PropTypes.arrayOf(PropTypes.shape({})),
}

ChartWidget.defaultProps = {
    toolbar: {},
    filter: {},
    chart: {},
}

ChartWidget.contextTypes = {
    resolveProps: PropTypes.func,
}

export default dependency(ChartWidget)
