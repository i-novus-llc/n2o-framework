import React from 'react'
import { compose, withHandlers, mapProps, defaultProps } from 'recompose'
import map from 'lodash/map'
import get from 'lodash/get'
import moment from 'moment'

import { withWidgetHandlers } from '../AdvancedTable/AdvancedTableContainer'
import { withContainerLiveCycle } from '../Table/withContainerLiveCycle'

import Calendar from './Calendar'
import { CalendarEvent } from './CalendarEvent'
import { CalendarCell } from './CalendarCell'
import { CalendarDateCell } from './CalendarDateCell'

function CalendarContainer(props) {
    return <Calendar {...props} />
}

CalendarContainer.defaultProps = {
    actionOnSelectEvent: {},
}

export { CalendarContainer }

export default compose(
    defaultProps({
        startFieldId: 'start',
        endFieldId: 'end',
        height: '1200px',
    }),
    withContainerLiveCycle,
    withWidgetHandlers,
    withHandlers({
        mapEvents: ({ startFieldId, endFieldId, step }) => events => map(events, event => ({
            ...event,
            step,
            [startFieldId]: new Date(event[startFieldId]),
            [endFieldId]: new Date(event[endFieldId]),
        })),
        createComponents: ({
            cell,
            cellColorFieldId,
            markDaysOff,
            setResolve,
            onSelectEvent,
            dispatch,
        }) => () => ({
            eventWrapper: eventProps => (
                <CalendarEvent
                    {...eventProps}
                    {...cell}
                    setResolve={setResolve}
                    onSelectEvent={onSelectEvent}
                    dispatch={dispatch}
                    cellColorAccessor={cellColorFieldId}
                />
            ),
            timeSlotWrapper: cellProps => (
                <CalendarCell {...cellProps} markDaysOff={markDaysOff} />
            ),
            dateCellWrapper: dateCellProps => (
                <CalendarDateCell {...dateCellProps} markDaysOff={markDaysOff} />
            ),
        }),
    }),
    mapProps(
        ({
            models,
            date = new Date(),
            startFieldId,
            endFieldId,
            titleFieldId,
            tooltipFieldId,
            allDayFieldId,
            cellColorFieldId,
            disabledFieldId,
            mapEvents,
            createComponents,
            defaultView,
            height,
            onSelectEvent,
            onSelectSlot,
            formats,
            views,
            timeSlots,
            selectable,
            minTime,
            maxTime,
            step,
            resources,
            messages,
            dispatch,
            setResolve,
        }) => ({
            events: mapEvents(models.datasource),
            startAccessor: startFieldId,
            endAccessor: endFieldId,
            titleAccessor: titleFieldId,
            tooltipAccessor: tooltipFieldId,
            defaultDate: new Date(date),
            allDayAccessor: allDayFieldId,
            cellColorAccessor: cellColorFieldId,
            disabledAccessor: disabledFieldId,
            components: createComponents(),
            defaultView,
            style: { height },
            onSelectEvent: (e) => {
                if (!e.disabled) {
                    setResolve({ id: get(e, 'id') })
                    dispatch(onSelectEvent)
                }
            },
            onSelectSlot: (e) => {
                if (get(e, 'start')) {
                    const dateIsSame = moment(get(e, 'start')).isSame(get(e, 'end'))
                    const currentData = {
                        start: moment(get(e, 'start')).format('YYYY-MM-DD HH:mm'),
                        end: dateIsSame
                            ? moment(get(e, 'end'))
                                .add(1, 'days')
                                .format('YYYY-MM-DD HH:mm')
                            : moment(get(e, 'end')).format('YYYY-MM-DD HH:mm'),
                        resourceId: get(e, 'resourceId'),
                    }

                    setResolve(currentData)
                    dispatch(onSelectSlot)
                }
            },
            formats,
            views,
            timeslots: timeSlots,
            selectable,
            minTime,
            maxTime,
            step,
            resources,
            messages,
        }),
    ),
)(CalendarContainer)
