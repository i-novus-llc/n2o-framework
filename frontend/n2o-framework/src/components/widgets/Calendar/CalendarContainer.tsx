import React from 'react'
import map from 'lodash/map'
import get from 'lodash/get'
import flowRight from 'lodash/flowRight'
import dayjs from 'dayjs'
import { connect } from 'react-redux'
import { Dispatch } from 'redux'

import { withWidgetHandlers } from '../hocs/withWidgetHandlers'
import { dataSourceModelByPrefixSelector } from '../../../ducks/datasource/selectors'
import { ModelPrefix } from '../../../core/datasource/const'
import { State } from '../../../ducks/State'

import { Calendar } from './Calendar'
import { CalendarEvent } from './CalendarEvent'
import { CalendarCell } from './CalendarCell'
import { CalendarDateCell } from './CalendarDateCell'
import { CalendarCellProps, type CalendarContainerProps, CalendarEventProps, CalendarEventType } from './types'

const createComponents = (
    setResolve: CalendarEventProps['setResolve'],
    onSelectEvent: CalendarEventProps['onSelectEvent'],
    dispatch: Dispatch,
    cellColorFieldId: string,
    markDaysOff: boolean,
) => ({
    eventWrapper: (eventProps: CalendarEventProps) => (
        <CalendarEvent
            {...eventProps}
            setResolve={setResolve}
            onSelectEvent={onSelectEvent}
            dispatch={dispatch}
            cellColorAccessor={cellColorFieldId}
        />
    ),
    timeSlotWrapper: (cellProps: CalendarCellProps) => (
        <CalendarCell {...cellProps} markDaysOff={markDaysOff} />
    ),
    dateCellWrapper: (dateCellProps: CalendarCellProps) => (
        <CalendarDateCell {...dateCellProps} markDaysOff={markDaysOff} />
    ),
})

const FORMAT = 'YYYY-MM-DD HH:mm'

function CalendarContainer({
    dataSourceModel,
    date = new Date(),
    startFieldId = 'start',
    endFieldId = 'end',
    titleFieldId,
    tooltipFieldId,
    allDayFieldId,
    cellColorFieldId,
    disabledFieldId,
    height = '1200px',
    onSelectEvent,
    onSelectSlot,
    dispatch,
    setResolve,
    formats,
    views,
    timeSlots,
    selectable,
    minTime,
    maxTime,
    step,
    resources,
    messages,
    defaultView,
    markDaysOff,
}: CalendarContainerProps) {
    const mapEvents = (events: CalendarContainerProps['dataSourceModel']) => map(events, event => ({
        ...event,
        step,
        [startFieldId]: new Date(event[startFieldId]),
        [endFieldId]: new Date(event[endFieldId]),
    }))

    const events = mapEvents(dataSourceModel)
    const components = createComponents(setResolve, onSelectEvent, dispatch, cellColorFieldId, markDaysOff)

    const handleSelectEvent = (e: CalendarEventType) => {
        if (!e.disabled) {
            setResolve({ id: get(e, 'id') })
            dispatch(onSelectEvent)
        }
    }

    const handleSelectSlot = (e: CalendarEventType) => {
        if (get(e, 'start')) {
            const dateIsSame = dayjs(get(e, 'start')).isSame(get(e, 'end'))
            const currentData = {
                start: dayjs(get(e, 'start')).format(FORMAT),
                end: dateIsSame
                    ? dayjs(get(e, 'end')).add(1, 'days').format(FORMAT)
                    : dayjs(get(e, 'end')).format(FORMAT),
                resourceId: get(e, 'resourceId'),
            }

            setResolve(currentData)
            dispatch(onSelectSlot)
        }
    }

    return (
        <Calendar
            events={events}
            startAccessor={startFieldId}
            endAccessor={endFieldId}
            titleAccessor={titleFieldId}
            tooltipAccessor={tooltipFieldId}
            defaultDate={new Date(date)}
            allDayAccessor={allDayFieldId}
            cellColorAccessor={cellColorFieldId}
            disabledAccessor={disabledFieldId}
            components={components}
            defaultView={defaultView} // Add a default value if necessary
            style={{ height }}
            onSelectEvent={handleSelectEvent}
            onSelectSlot={handleSelectSlot}
            formats={formats}
            views={views}
            timeslots={timeSlots}
            selectable={selectable}
            minTime={minTime}
            maxTime={maxTime}
            step={step}
            resources={resources}
            messages={messages}
        />
    )
}

const mapStateToProps = (state: State, { datasource }: CalendarContainerProps) => ({
    dataSourceModel: dataSourceModelByPrefixSelector(datasource, ModelPrefix.source)(state),
})

export default flowRight(
    connect(mapStateToProps),
    withWidgetHandlers,
)(CalendarContainer)
