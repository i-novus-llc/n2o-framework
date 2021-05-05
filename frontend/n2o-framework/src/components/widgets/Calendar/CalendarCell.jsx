import React from 'react'
import PropTypes from 'prop-types'
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

CalendarCell.propTypes = {
    value: PropTypes.any,
    markDaysOff: PropTypes.any,
    children: PropTypes.any,
}

export default CalendarCell
