import React from 'react'
import { Dayjs } from 'dayjs'
import classNames from 'classnames'

import { TBaseProps } from '../../types'

import '../../styles/components/Day.scss'

type DayProps = TBaseProps & {
    current?: boolean,
    day: Dayjs,
    inputName: string,
    otherMonth?: boolean,
    select(day: Dayjs, name: string): void,
    selected?: boolean,
}

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
        if (!disabled) {
            select(day, inputName)
        }
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
