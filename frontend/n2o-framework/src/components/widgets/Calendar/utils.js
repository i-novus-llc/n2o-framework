import { momentLocalizer } from 'react-big-calendar';
import moment from 'moment';

export function isDayOff(day) {
  return [0, 6].indexOf(day.getDay()) !== -1;
}

export function isCurrentDay(day) {
  const currentDate = new Date();
  return (
    day.getDate() === currentDate.getDate() &&
    day.getMonth() === currentDate.getMonth() &&
    day.getFullYear() === currentDate.getFullYear()
  );
}

export function formatsMap(formats = {}) {
  const localizerFormat = (date, format) =>
    momentLocalizer(moment).format(date, format);

  const rangeFormat = (startFormat, endFormat) => ({ start, end }) =>
    localizerFormat(start, startFormat) +
    ' — ' +
    localizerFormat(end, endFormat);

  const {
    dateFormat = 'DD',
    dayFormat = 'DD ddd',
    weekdayFormat = 'ddd',
    timeStartFormat = 'HH:mm',
    timeEndFormat = 'HH:mm',
    dayStartFormat = 'DD MMM',
    dayEndFormat = 'DD MMM',
    timeGutterFormat = 'LT',
    monthHeaderFormat = 'MMMM YYYY',
    dayHeaderFormat = 'dddd MMM DD',
    agendaDateFormat = 'ddd MMM DD',
    agendaTimeFormat = 'LT',
  } = formats;

  return {
    dateFormat,
    dayFormat: date => localizerFormat(date, dayFormat),
    weekdayFormat: date => localizerFormat(date, weekdayFormat), // формат дня недели при view: 'month'
    timeGutterFormat: date => localizerFormat(date, timeGutterFormat),
    selectRangeFormat: rangeFormat(timeStartFormat, timeEndFormat),
    eventTimeRangeFormat: rangeFormat(timeStartFormat, timeEndFormat),
    monthHeaderFormat: date => localizerFormat(date, monthHeaderFormat), // формат заголовка при view: 'month'
    dayHeaderFormat: date => localizerFormat(date, dayHeaderFormat),
    dayRangeHeaderFormat: rangeFormat(dayStartFormat, dayEndFormat),
    agendaHeaderFormat: rangeFormat(dayStartFormat, dayEndFormat),
    agendaDateFormat: date => localizerFormat(date, agendaDateFormat),
    agendaTimeFormat: date => localizerFormat(date, agendaTimeFormat),
    agendaTimeRangeFormat: rangeFormat(timeStartFormat, timeEndFormat),
  };
}
