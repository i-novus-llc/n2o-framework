import React, { useContext, useMemo } from 'react'

import { WidgetHOC } from '../../../core/widget/WidgetHOC'
import { FactoryContext } from '../../../core/factory/context'
import StandardWidget from '../StandardWidget'
import { StandardFieldset } from '../Form/fieldsets'
import { WithActiveModel } from '../Widget/WithActiveModel'

import CalendarContainer from './CalendarContainer'
import { type CalendarWidgetProps } from './types'

function CalendarWidget(props: CalendarWidgetProps) {
    const {
        id: widgetId,
        datasource,
        toolbar,
        disabled,
        className,
        style,
        filter,
        calendar,
        loading,
    } = props
    const { resolveProps } = useContext(FactoryContext)
    const resolvedFilter = useMemo(() => resolveProps(filter || {}, StandardFieldset) as CalendarWidgetProps['filter'], [filter, resolveProps])

    return (
        <StandardWidget
            disabled={disabled}
            widgetId={widgetId}
            datasource={datasource}
            toolbar={toolbar}
            filter={resolvedFilter}
            className={className}
            style={style}
            loading={loading}
        >
            <CalendarContainer {...props} {...calendar} />
        </StandardWidget>
    )
}

export default WidgetHOC(WithActiveModel<CalendarWidgetProps>(CalendarWidget))
