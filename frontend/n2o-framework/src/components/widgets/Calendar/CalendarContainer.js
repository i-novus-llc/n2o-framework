import React from 'react';
import { compose, withHandlers, mapProps, defaultProps } from 'recompose';
import map from 'lodash/map';
import get from 'lodash/get';
import moment from 'moment';

import widgetContainer from '../WidgetContainer';
import { withWidgetHandlers } from '../AdvancedTable/AdvancedTableContainer';
import { withContainerLiveCycle } from '../Table/TableContainer';
import Calendar from './Calendar';
import CalendarEvent from './CalendarEvent';
import CalendarCell from './CalendarCell';
import CalendarDateCell from './CalendarDateCell';

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
    height: '1200px',
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
    mapEvents: ({ startFieldId, endFieldId, step }) => events =>
      map(events, event => ({
        ...event,
        step,
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
      onSelectEvent,
      onSelectSlot,
      formats,
      views,
      timeSlots,
      selectable,
      maxDate,
      minDate,
      step,
      resources,
      messages,
      dispatch,
      onResolve,
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
        if (!e.disabled) {
          onResolve({ id: get(e, 'id') });
          dispatch(onSelectEvent);
        }
      },
      actionOnClickSlot: e => {
        if (get(e, 'start')) {
          const currentData = {
            start: moment(get(e, 'start')).format('YYYY-MM-DD HH:mm'),
            end: moment(get(e, 'end')).format('YYYY-MM-DD HH:mm'),
            resourceId: get(e, 'resourceId'),
          };
          onResolve(currentData);
          dispatch(onSelectSlot);
        }
      },
      formats,
      views,
      timeslots: timeSlots,
      selectable,
      maxDate,
      minDate,
      step,
      resources,
      messages,
    })
  )
)(CalendarContainer);
