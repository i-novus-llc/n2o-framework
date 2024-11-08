import React from 'react'
import classNames from 'classnames'

import { isDayOff } from './utils'

export function CalendarCell({ value, markDaysOff, children }) {
    return (
        <div
            className={classNames('calendar__cell', {
                'calendar__cell--day-off': isDayOff(value) && markDaysOff,
            })}
        >
            {children}
        </div>
    )
}

export default CalendarCell
