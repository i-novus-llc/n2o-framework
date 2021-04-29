import React from 'react'
import PropTypes from 'prop-types'
import { Calendar as BigCalendar, momentLocalizer } from 'react-big-calendar'
import moment from 'moment'
import classNames from 'classnames'
import { getContext } from 'recompose'
import { useTranslation } from 'react-i18next'

import { formatsMap, timeParser } from './utils'

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
 * @reactProps {number} step - шаг времени в календаре (30 = 1 час)
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
    minTime,
    maxTime,
    step,
    resources,
    onSelectEvent,
    onSelectSlot,
    configLocale,
}) {
    const { t } = useTranslation()

    moment.locale(configLocale)

    const localizer = momentLocalizer(moment)

    const messages = {
        month: t('calendarMonth'),
        day: t('calendarDay'),
        today: t('calendarToday'),
        week: t('calendarWeek'),
        agenda: t('calendarAgenda'),
        next: t('calendarNext'),
        previous: t('calendarPrevious'),
        noEventsInRange: t('calendarNoEventsInRange'),
        tomorrow: t('calendarTomorrow'),
        work_week: t('calendarWorkweek'),
        yesterday: t('calendarYesterday'),
        event: t('calendarEvent'),
        allDay: t('calendarAllDay'),
        date: t('calendarDate'),
        time: t('calendarTime'),
    }

    return (
        <BigCalendar
            className={classNames('calendar', className)}
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
            resources={resources}
            onSelectEvent={onSelectEvent}
            onSelectSlot={onSelectSlot}
            messages={messages}
            {...timeParser(minTime, maxTime)}
        />
    )
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
    configLocale: PropTypes.string,
    defaultView: PropTypes.string,
    t: PropTypes.func,
    onSelectEvent: PropTypes.func,
    onSelectSlot: PropTypes.func,
    components: PropTypes.any,
    minTime: PropTypes.any,
    maxTime: PropTypes.any,
}
Calendar.defaultProps = {
    startAccessor: 'start',
    endAccessor: 'end',
    events: [],
    configLocale: 'ru',
    t: () => {},
}

export default getContext({
    configLocale: PropTypes.string,
})(Calendar)
