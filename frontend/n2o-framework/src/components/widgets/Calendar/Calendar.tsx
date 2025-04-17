import React from 'react'
// @ts-ignore import from js file react-big-calendar
import { Calendar as BigCalendar, dayjsLocalizer } from 'react-big-calendar'
import dayjs from 'dayjs'
import localizedFormat from 'dayjs/plugin/localizedFormat'
import localeData from 'dayjs/plugin/localeData'
import 'dayjs/locale/ru'
import classNames from 'classnames'
import { useTranslation } from 'react-i18next'

import { EMPTY_ARRAY } from '../../../utils/emptyTypes'

import { formatsMap, timeParser } from './utils'
import { CalendarProps } from './types'

dayjs.extend(localizedFormat)
dayjs.extend(localeData)

const localizer = dayjsLocalizer(dayjs)
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

export function Calendar({
    className,
    defaultDate,
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
    events = EMPTY_ARRAY,
    startAccessor = 'start',
    endAccessor = 'end',
    configLocale = 'ru',
}: CalendarProps) {
    const { t } = useTranslation()

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

    dayjs.locale(configLocale)

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

export default Calendar
