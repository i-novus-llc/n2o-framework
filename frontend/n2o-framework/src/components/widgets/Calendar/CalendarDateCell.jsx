import React from 'react'
import classNames from 'classnames'
import PropTypes from 'prop-types'

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

CalendarDateCell.propTypes = {
    children: PropTypes.any,
    value: PropTypes.any,
    markDaysOff: PropTypes.any,
}

export default CalendarDateCell
