import React from 'react'
import cn from 'classnames'

import { isDayOff } from './utils'

function CalendarCell({ value, markDaysOff, children }) {
    return (
        <div
            className={cn('calendar__cell', {
                'calendar__cell--day-off': isDayOff(value) && markDaysOff,
            })}
        >
            {children}
        </div>
    )
}

export default CalendarCell
