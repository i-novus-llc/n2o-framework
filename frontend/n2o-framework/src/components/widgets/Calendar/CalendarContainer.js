import React from 'react';
import { compose, withHandlers, mapProps, defaultProps } from 'recompose';
import map from 'lodash/map';
import PropTypes from 'prop-types';
import cn from 'classnames';

import widgetContainer from '../WidgetContainer';
import CalendarWidgetToolbar from './CalendarWidgetToolbar';
import { withWidgetHandlers } from '../AdvancedTable/AdvancedTableContainer';
import { withContainerLiveCycle } from '../Table/TableContainer';
import Calendar from './Calendar';
import CalendarEvent from './CalendarEvent';
import CalendarCell from './CalendarCell';

function CalendarContainer(props) {
  return <Calendar {...props} />;
}

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
    createComponents: ({
      widgetId,
      toolbar,
      views,
      cell,
      cellColorFieldId,
      markDaysOff,
    }) => () => ({
      toolbar: toolbarProps => (
        <CalendarWidgetToolbar
          {...toolbarProps}
          toolbar={toolbar}
          widgetId={widgetId}
          views={views}
        />
      ),
      eventWrapper: eventProps => (
        <CalendarEvent
          {...eventProps}
          {...cell}
          cellColorAccessor={cellColorFieldId}
        />
      ),
      dateCellWrapper: cellProps => <CalendarCell {...cellProps} />,
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
    })
  )
)(CalendarContainer);
