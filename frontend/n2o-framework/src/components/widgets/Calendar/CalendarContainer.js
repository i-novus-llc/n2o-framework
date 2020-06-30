import React from 'react';
import { compose, withHandlers, mapProps, defaultProps } from 'recompose';
import map from 'lodash/map';

import widgetContainer from '../WidgetContainer';
import { withWidgetHandlers } from '../AdvancedTable/AdvancedTableContainer';
import { withContainerLiveCycle } from '../Table/TableContainer';
import Calendar from './Calendar';
import CalendarEvent from './CalendarEvent';
import CalendarCell from './CalendarCell';
import CalendarDateCell from './CalendarDateCell';

const eventType = {
  CLICK: 'click',
};

function CalendarContainer(props) {
  return <Calendar {...props} />;
}

CalendarContainer.defaultProps = {
  actionOnSelectEvent: {},
};

export { CalendarContainer };

export default compose(
  defaultProps({
    startFieldId: 'start',
    endFieldId: 'end',
  }),
  widgetContainer(
    {
      mapProps: ({ calendar, ...props }) => ({
        ...props,
        ...calendar,
      }),
    },
    'CalendarWidget'
  ),
  withContainerLiveCycle,
  withWidgetHandlers,
  withHandlers({
    mapEvents: ({ startFieldId, endFieldId }) => events =>
      map(events, event => ({
        ...event,
        [startFieldId]: new Date(event[startFieldId]),
        [endFieldId]: new Date(event[endFieldId]),
      })),
    createComponents: ({ cell, cellColorFieldId, markDaysOff }) => () => ({
      eventWrapper: eventProps => (
        <CalendarEvent
          {...eventProps}
          {...cell}
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
      unfunc,
      datasource,
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
      actionOnSelectEvent,
      formats,
      views,
      timeslots,
      selectable,
      maxDate,
      minDate,
      step,
      resources,
      messages,
      dispatch,
    }) => ({
      events: mapEvents(datasource),
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
      actionOnClickEvent: e => {
        if (!e.disabled) dispatch(actionOnSelectEvent);
      },
      actionOnClickSlot: e => {
        if (e.action === eventType.CLICK) dispatch(actionOnSelectEvent);
      },
      formats,
      views,
      timeslots,
      selectable,
      maxDate,
      minDate,
      step,
      resources,
      messages,
    })
  )
)(CalendarContainer);
