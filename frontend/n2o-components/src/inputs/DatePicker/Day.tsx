import React from 'react'
import classNames from 'classnames'

import '../../styles/components/Day.scss'
import { type DayProps } from './types'

export const Day = ({
    disabled = false,
    selected = false,
    otherMonth = false,
    current,
    day,
    inputName,
    select,
}: DayProps) => {
    const onClick = () => {
        if (!disabled) { select(day, inputName) }
    }

    return (
        // eslint-disable-next-line jsx-a11y/no-noninteractive-element-interactions
        <td
            onMouseDown={onClick}
            className={classNames('n2o-calendar-day', {
                disabled, selected, current, 'other-month': otherMonth,
            })}
        >
            {day.format('D')}
        </td>
    )
}
