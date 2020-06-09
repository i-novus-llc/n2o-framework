import React from 'react';

const mapStyle = ({ height, top, width } = {}, color) => ({
  position: 'absolute',
  height: height + '%',
  top: top + '%',
  width: width + '%',
  backgroundColor: color,
});

const DEFAULT_BG_COLOR = '#3174ad';

function CalendarEvent({ style, label, event, accessors, cellColorAccessor }) {
  console.log('point');
  console.log(arguments[0]);
  const tooltip = accessors.tooltip(event);
  const title = accessors.title(event);
  const color = event[cellColorAccessor] || DEFAULT_BG_COLOR;

  return (
    <div
      className="calendar__event"
      style={style && mapStyle(style, color)}
      title={tooltip}
    >
      <div className="calendar__event-label">{label}</div>
      <div className="calendar__event-name">{title}</div>
    </div>
  );
}

export default CalendarEvent;
