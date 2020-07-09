import React from 'react';
import get from 'lodash/get';
import { eventLessHour } from './utils';

const mapStyle = ({ height, top, width } = {}, color, lessHour) => ({
  position: 'absolute',
  height: height + '%',
  top: top + '%',
  width: width + '%',
  backgroundColor: color,
  padding: lessHour ? '0 5px' : '2px 5px',
  lineHeight: lessHour ? '1' : '1.5',
});

const monthEventStyle = color => ({
  backgroundColor: color,
});

const DEFAULT_BG_COLOR = '#3174ad';

function CalendarEvent({
  style,
  label,
  event,
  accessors,
  cellColorAccessor,
  onClick,
}) {
  const tooltip = accessors.tooltip(event);
  const title = accessors.title(event);
  const color = event[cellColorAccessor] || DEFAULT_BG_COLOR;
  const lessHour = eventLessHour(get(event, 'date'));

  return (
    <div
      className="calendar__event rbc-event"
      style={style ? mapStyle(style, color, lessHour) : monthEventStyle(color)}
      title={tooltip}
      onClick={onClick}
    >
      {!lessHour && <div className="calendar__event-label">{label}</div>}
      <div className="calendar__event-name">{title}</div>
    </div>
  );
}

export default CalendarEvent;
