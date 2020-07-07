import React from 'react';
import get from 'lodash/get';
import { eventLessHour } from './utils';

const mapStyle = ({ height, top, width } = {}, color) => ({
  position: 'absolute',
  height: 'min-content',
  top: top + '%',
  width: width + '%',
  backgroundColor: color,
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

  return (
    <div
      className="calendar__event"
      style={style ? mapStyle(style, color) : monthEventStyle(color)}
      title={tooltip}
      onClick={onClick}
    >
      {!eventLessHour(get(event, 'date')) && (
        <div className="calendar__event-label">{label}</div>
      )}
      <div className="calendar__event-name">{title}</div>
    </div>
  );
}

export default CalendarEvent;
