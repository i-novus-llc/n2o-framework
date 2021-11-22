import React, { useContext, useMemo } from 'react'
import PropTypes from 'prop-types'

import { WidgetHOC } from '../../../core/widget/Widget'
import { widgetPropTypes } from '../../../core/widget/propTypes'
import { FactoryContext } from '../../../core/factory/context'
import StandardWidget from '../StandardWidget'
import { StandardFieldset } from '../Form/fieldsets'

// eslint-disable-next-line import/no-named-as-default
import CalendarContainer from './CalendarContainer'

function CalendarWidget(props) {
    const {
        id: widgetId,
        datasource,
        toolbar,
        disabled,
        className,
        style,
        filter,
        calendar,
    } = props
    const { resolveProps } = useContext(FactoryContext)
    const resolvedFilter = useMemo(() => resolveProps(filter, StandardFieldset), [filter, resolveProps])

    return (
        <StandardWidget
            disabled={disabled}
            widgetId={widgetId}
            datasource={datasource}
            toolbar={toolbar}
            filter={resolvedFilter}
            className={className}
            style={style}
        >
            <CalendarContainer
                {...props}
                {...calendar}
            />
        </StandardWidget>
    )
}

CalendarWidget.propTypes = {
    ...widgetPropTypes,
    calendar: PropTypes.any,
}

export default WidgetHOC(CalendarWidget)
