import React from 'react'
import PropTypes from 'prop-types'

import Toolbar from '../../buttons/Toolbar'

// пока не используется
function CalendarWidgetToolbar({ widgetId, toolbar, label }) {
    return (
        <div className="calendar__toolbar">
            <div className="calendar__toolbar--left">
                <Toolbar entityKey={widgetId} toolbar={toolbar.calendarTopLeft} />
            </div>
            <div className="calendar__toolbar--center">{label}</div>
            <div className="calendar__toolbar--right">
                <Toolbar entityKey={widgetId} toolbar={toolbar.calendarTopRight} />
            </div>
        </div>
    )
}

CalendarWidgetToolbar.propTypes = {
    widgetId: PropTypes.string,
    toolbar: PropTypes.object,
}

CalendarWidgetToolbar.defaultProps = {
    toolbar: {},
}

export default CalendarWidgetToolbar
