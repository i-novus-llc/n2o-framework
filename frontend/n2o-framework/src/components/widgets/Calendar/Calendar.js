import React from 'react';
import PropTypes from 'prop-types';
import { Calendar as BigCalendar, momentLocalizer } from 'react-big-calendar';
import moment from 'moment';
import cn from 'classnames';

const localizer = momentLocalizer(moment);

function Calendar({
  className,
  events,
  defaultDate,
  startAccessor,
  endAccessor,
  titleAccessor,
  tooltipAccessor,
  defaultView,
  style,
  components,
}) {
  // console.log('point');
  // console.log(arguments);
  return (
    <BigCalendar
      className={cn('calendar', className)}
      localizer={localizer}
      events={events}
      startAccessor={startAccessor}
      endAccessort={endAccessor}
      titleAccessor={titleAccessor}
      tooltipAccessor={tooltipAccessor}
      defaultDate={defaultDate}
      style={style}
      step={60}
      components={components}
      defaultView={defaultView}
    />
  );
}

Calendar.propTypes = {
  events: PropTypes.array,
  startAccessor: PropTypes.string,
  endAccessor: PropTypes.string,
};
Calendar.defaultProps = {
  startAccessor: 'start',
  endAccessor: 'end',
  events: [],
};

export default Calendar;
