import React from 'react'
import classNames from 'classnames'

import { isDayOff, isCurrentDay } from './utils'

export function CalendarDateCell({ children, value, markDaysOff }) {
    return (
        <div
            className={classNames('calendar__cell calendar__cell--day-cell', {
                'calendar__cell--day-off': isDayOff(value) && markDaysOff,
                'calendar__cell--current-day': isCurrentDay(value),
            })}
        >
            {children}
        </div>
    )
}

export default CalendarDateCell
