import { CSSProperties, ReactNode } from 'react'
import { Action, Dispatch } from 'redux'

import { type Props as StandardWidgetProps } from '../StandardWidget'
import { type WidgetProps } from '../Widget/WithActiveModel'

export interface CalendarCellProps {
    value: Date
    markDaysOff: boolean
    children?: ReactNode
}

export type CalendarEventType = Record<string, never>

export interface CalendarEventProps {
    style?: CSSProperties;
    event: CalendarEventType
    accessors: {
        tooltip(event: CalendarEventType): string
        title(event: CalendarEventType): string
    };
    cellColorAccessor: string;
    setResolve(model: Record<string, unknown>): void
    onSelectEvent: Action
    dispatch: Dispatch
}

export interface CalendarProps {
    className?: string
    defaultDate: Date
    titleAccessor: string
    tooltipAccessor: string
    allDayAccessor: string
    cellColorAccessor: string
    disabledAccessor: string
    defaultView: string
    messages: string[]
    style?: CSSProperties
    components: {
        eventWrapper(eventProps: CalendarEventProps): JSX.Element
        timeSlotWrapper(cellProps: CalendarCellProps): JSX.Element
        dateCellWrapper(dateCellProps: CalendarCellProps): JSX.Element
    }
    views: string
    formats: Record<string, string>
    timeslots: string
    selectable: string
    minTime: string
    maxTime: string
    step: string
    resources: string
    onSelectEvent(event: CalendarEventType): void
    onSelectSlot(event: CalendarEventType): void
    events: Array<Record<string, string | Date>>
    startAccessor: string
    endAccessor: string
    configLocale?: string
}

export interface CalendarContainerProps {
    datasource: string
    dataSourceModel: CalendarProps['events']
    date: Date
    startFieldId: string
    endFieldId: string
    titleFieldId: string
    tooltipFieldId: string
    allDayFieldId: string
    cellColorFieldId: string
    disabledFieldId: string
    height: string
    onSelectEvent: CalendarEventProps['onSelectEvent']
    onSelectSlot: Action
    dispatch: Dispatch
    setResolve: CalendarEventProps['setResolve']
    formats: CalendarProps['formats']
    views: string
    timeSlots: string
    selectable: string
    minTime: string
    maxTime: string
    step: string
    resources: string
    messages: CalendarProps['messages']
    defaultView: string
    onRowClickAction(): void
    markDaysOff: CalendarCellProps['markDaysOff']
}

export interface CalendarWidgetProps extends StandardWidgetProps {
    id: string
    className: string
    calendar: CalendarContainerProps
    setResolve: WidgetProps['setResolve']
}
