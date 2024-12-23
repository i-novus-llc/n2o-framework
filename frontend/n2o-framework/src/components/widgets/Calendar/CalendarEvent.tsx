import React from 'react'
import get from 'lodash/get'
import dayjs from 'dayjs'
import classNames from 'classnames'

import { eventLessHour, isAllDay, mapStyle, monthEventStyle } from './utils'
import { CalendarEventProps } from './types'

export function CalendarEvent({
    style,
    event,
    accessors,
    cellColorAccessor,
    setResolve,
    onSelectEvent,
    dispatch,
}: CalendarEventProps) {
    const tooltip = accessors.tooltip(event)
    const title = accessors.title(event)
    const color = event[cellColorAccessor] || '#3174ad'
    const lessHour = eventLessHour(get(event, 'date'), get(event, 'step'))
    const begin = get(event, 'date.begin')
    const end = get(event, 'date.end')
    const disabled = get(event, 'disabled', false)

    const handleClick = () => {
        setResolve({ id: get(event, 'id') })
        dispatch(onSelectEvent)
    }

    return (
        <div
            className="calendar__event rbc-event"
            style={style ? mapStyle(color, lessHour, style) : monthEventStyle(color)}
            title={tooltip}
            onClick={!disabled ? handleClick : undefined}
        >
            <div
                className={classNames('calendar__event-name', {
                    'calendar__event-name--nowrap': lessHour,
                })}
            >
                {title}
            </div>
            <div className="calendar__event-label">
                {!isAllDay(begin, end) ? dayjs(begin).format('HH:mm') : null}
            </div>
        </div>
    )
}

export default CalendarEvent
