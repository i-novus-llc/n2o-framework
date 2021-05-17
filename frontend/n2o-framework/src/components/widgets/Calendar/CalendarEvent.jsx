import React from 'react'
import get from 'lodash/get'
import moment from 'moment'
import classNames from 'classnames'
import PropTypes from 'prop-types'

import { eventLessHour, isAllDay } from './utils'

const mapStyle = ({ height, top, width } = {}, color, lessHour) => ({
    position: 'absolute',
    height: `${height}%`,
    top: `${top}%`,
    width: `${width}%`,
    backgroundColor: color,
    padding: lessHour ? '0 5px' : '2px 5px',
    lineHeight: lessHour ? '1' : '1.5',
    flexFlow: lessHour ? 'nowrap' : 'none',
})

const monthEventStyle = color => ({
    backgroundColor: color,
})

const DEFAULT_BG_COLOR = '#3174ad'

export function CalendarEvent({
    style,
    event,
    accessors,
    cellColorAccessor,
    onResolve,
    onSelectEvent,
    dispatch,
}) {
    const tooltip = accessors.tooltip(event)
    const title = accessors.title(event)
    const color = event[cellColorAccessor] || DEFAULT_BG_COLOR
    const lessHour = eventLessHour(get(event, 'date'), get(event, 'step'))
    const begin = get(event, 'date.begin')
    const end = get(event, 'date.end')
    const disabled = get(event, 'disabled', false)

    const handleClick = () => {
        onResolve({ id: get(event, 'id') })
        dispatch(onSelectEvent)
    }

    return (
        <div
            className="calendar__event rbc-event"
            style={style ? mapStyle(style, color, lessHour) : monthEventStyle(color)}
            title={tooltip}
            onClick={!disabled ? handleClick : null}
        >
            <div
                className={classNames('calendar__event-name', {
                    'calendar__event-name--nowrap': lessHour,
                })}
            >
                {title}
            </div>
            <div className="calendar__event-label">
                {!isAllDay(begin, end) ? moment(begin).format('HH:mm') : null}
            </div>
        </div>
    )
}

CalendarEvent.propTypes = {
    style: PropTypes.object,
    event: PropTypes.object,
    accessors: PropTypes.object,
    cellColorAccessor: PropTypes.any,
    onResolve: PropTypes.func,
    onSelectEvent: PropTypes.func,
    dispatch: PropTypes.func,
}

export default CalendarEvent
