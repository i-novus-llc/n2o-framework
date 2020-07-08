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
 * @reactProps {object} tooltipAccessor - аксессор для всплывающей подсказки
 * @reactProps {object} style - стили календаря
 * @reactProps {object} components - настройте отображение различных разделов календаря, предоставив настраиваемые компоненты
 * @reactProps {object} views - массив имен встроенных представлений
 * @reactProps {object} formats - форматы отображения дат
 * @reactProps {number} step - шаг времени в календаре (15 = 1 час)
 * @reactProps {number} timeslots - количество слотов в ячейке (шаге)
 * @reactProps {bool} selectable - возможность выбрать даты путем выделения
 * @reactProps {object} resources - разбиение не по дням, а по каким либо сущностям
 * @reactProps {string} minDate - ограничивает минимальное время просмотра дня и недели
 * @reactProps {string} maxDate - ограничивает максимальное время просмотра дня и недели
 * @reactProps {object} messages - переопределение названия кнопок действий (прим. messages: { month: 'Месяц', })
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
  step,
  resources,
  actionOnClickEvent,
  actionOnClickSlot,
  messages,
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
      defaultView={defaultView}
      views={views}
      formats={formatsMap(formats)}
      timeslots={timeslots}
      selectable={selectable}
      max={maxDate}
      min={minDate}
      resources={resources}
      onSelectEvent={e => actionOnClickEvent(e)}
      onSelectSlot={e => actionOnClickSlot(e)}
      messages={messages}
    />
  );
}

Calendar.propTypes = {
  /**
   * класс таблицы
   */
  className: PropTypes.string,
  /**
   * массив объектов событий для отображения в календаре
   */
  events: PropTypes.array,
  /**
   * дата / время начала мероприятия
   */
  startAccessor: PropTypes.string,
  /**
   * дата / время окончания мероприятия
   */
  endAccessor: PropTypes.string,
  /**
   * аксессор для заголовка события, используемый для отображения информации о событии
   */
  titleAccessor: PropTypes.string,
  /**
   * аксессор для всплывающей подсказки
   */
  tooltipAccessor: PropTypes.string,
  /**
   * дата
   */
  defaultDate: PropTypes.string,
  /**
   * стили календаря
   */
  style: PropTypes.object,
  /**
   * шаг времени в календаре (15 = 1 час)
   */
  step: PropTypes.number,
  /**
   * массив имен встроенных представлений
   */
  views: PropTypes.array,
  /**
   *  форматы отображения дат
   */
  formats: PropTypes.object,
  /**
   * количество слотов в ячейке (шаге)
   */
  timeslots: PropTypes.number,
  /**
   * возможность выбрать даты путем выделения
   */
  selectable: PropTypes.bool,
  /**
   * ограничивает максимальное время просмотра дня и недели
   */
  maxDate: PropTypes.string,
  /**
   * ограничивает минимальное время просмотра дня и недели
   */
  minDate: PropTypes.string,
  /**
   * разбиение не по дням, а по каким либо сущностям
   */
  resources: PropTypes.array,
  /**
   * переопределение названия кнопок действий (прим. messages: { month: 'Месяц', })
   */
  messages: PropTypes.object,
};
Calendar.defaultProps = {
  startAccessor: 'start',
  endAccessor: 'end',
  events: [],
  messages: {
    month: 'Месяц',
    day: 'День',
    today: 'Сегодня',
    week: 'Неделя',
    agenda: 'Повестка дня',
    next: 'Вперед',
    previous: 'Назад',
    noEventsInRange: 'На данном отрезке времени события отсутствуют',
    tomorrow: 'Завтра',
    work_week: 'Рабочая неделя',
    yesterday: 'Вчера',
    event: 'Событие',
    allDay: 'Весь день',
    date: 'Дата',
    time: 'Время',
  },
};

export default Calendar;
