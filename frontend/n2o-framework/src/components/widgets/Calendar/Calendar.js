import React from 'react';
import PropTypes from 'prop-types';
import { Calendar as BigCalendar, momentLocalizer } from 'react-big-calendar';
import moment from 'moment';
import cn from 'classnames';
import { formatsMap } from './utils';

const localizer = momentLocalizer(moment);

/**
 * Компонент Календарь
 * @reactProps {Array} events - массив объектов событий для отображения в календаре
 * @reactProps {string} className - класс таблицы
 * @reactProps {object} defaultDate - дата
 * @reactProps {string} startAccessor - дата / время начала мероприятия
 * @reactProps {string} endAccessor - дата / время окончания мероприятия
 * @reactProps {string} titleAccessor - аксессор для заголовка события, используемый для отображения информации о событии
 * @reactProps {object} tooltipAccessor - аксессор для всплывающей подсказки.
 * @reactProps {object} style - стили календаря
 * @reactProps {object} components - настройте отображение различных разделов календаря, предоставив настраиваемые компоненты
 * @reactProps {object} formats - форматы отображения дат
 * @reactProps {number} step - шаг времени в календаре (15 = 1 час)
 * @reactProps {number} timeslots - количество слотов в ячейке (шаге)
 * @reactProps {bool} selectable - возможность выбрать даты путем выделения
 * @reactProps {object} resources - разбиение не по дням, а по каким либо сущностям
 */

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
  views,
  formats,
  timeslots,
  selectable,
  maxDate,
  minDate,
  date,
  step,
  resources,
  actionOnClickEvent,
  actionOnClickSlot,
}) {
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
      step={step}
      components={components}
      defaultView={'week'}
      date={date}
      views={views}
      formats={formatsMap(formats)}
      timeslots={timeslots}
      selectable={selectable}
      max={maxDate}
      min={minDate}
      resources={resources}
      onSelectEvent={e => actionOnClickEvent(e)}
      onSelectSlot={e => actionOnClickSlot(e)}
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
