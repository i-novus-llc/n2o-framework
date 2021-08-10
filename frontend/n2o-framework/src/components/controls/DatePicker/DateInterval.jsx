import React from 'react'
import PropTypes from 'prop-types'
import moment from 'moment'
import defaults from 'lodash/defaults'
import { getContext } from 'recompose'

import DateTimeControl from './DateTimeControl'

/**
 * Компонент для выбора временного интервала. Состоит 2 {@link DatePicker}
 * @reactProps {object} defaultTime
 * @reactProps {array} value - массив, объекты которого задают дефолтные значения, имя ('beginDate' или 'endDate') и дефолтное время каждому пикеру.
 * @reactProps {string|moment|Date} min - самая ранняя доступная даустанавливает времята
 * @reactProps {string|moment|Date} max - самая поздняя доступная дата
 * @reactProps {function} onChange - вызывается при изменении
 * @reactProps {string} dateFormat - формат даты. Пример: "DD-MM_YYYY"
 * @reactProps {string} timeFormat - формат времени. Пример: "HH:mm"
 * @reactProps {string} outputFormat - формат даты при сохранение. Пример: "DD-MM_YYYY HH~mm"
 * @reactProps {boolean} disabled - задизейблен пикер / нет
 * @reactProps {string} placeholder - плэйсхолдер для поля
 * @reactProps {string} locale - Локаль. Варианты: 'en', 'ru'
 * @reactProps {boolean} openOnFocus - открывать при фокусе
 * @example
 * <DatePicker  defaultTime = '12:11'/>
 */
function DateInterval({
    value,
    defaultTime,
    defaultValue,
    onChange,
    onBlur,
    config,
    ...rest
}) {
    const newValue = defaults(value, defaultValue)

    const handleChange = (data) => {
        onChange({
            [DateTimeControl.beginInputName]: data[0],
            [DateTimeControl.endInputName]: data[1],
        })
    }

    const handleBlur = (data) => {
        onBlur({
            [DateTimeControl.beginInputName]: data[0],
            [DateTimeControl.endInputName]: data[1],
        })
    }

    const mappedValue = [
        {
            name: DateTimeControl.beginInputName,
            value: newValue[DateTimeControl.beginInputName],
            defaultTime,
        },
        {
            name: DateTimeControl.endInputName,
            value: newValue[DateTimeControl.endInputName],
            defaultTime,
        },
    ]

    return (
        <DateTimeControl
            {...rest}
            strategy="fixed"
            value={mappedValue}
            onChange={handleChange}
            onBlur={handleBlur}
            type="date-interval"
        />
    )
}

DateInterval.defaultProps = {
    defaultValue: {
        [DateTimeControl.beginInputName]: null,
        [DateTimeControl.endInputName]: null,
    },
    onChange: () => {},
    onFocus: () => {},
    onBlur: () => {},
    dateFormat: 'DD/MM/YYYY',
    placeholder: '',
    disabled: false,
    dateDivider: ' ',
    className: '',
    configLocale: 'ru',
    openOnFocus: false,
}

DateInterval.propTypes = {
    /**
     * Callback фокуса
     */
    onFocus: PropTypes.func,
    /**
     * Callback потери фокуса
     */
    onBlur: PropTypes.func,
    /**
     * Начальное время
     */
    defaultTime: PropTypes.object,
    /**
     * Значение контрола
     */
    value: PropTypes.oneOfType([
        PropTypes.instanceOf(moment),
        PropTypes.instanceOf(Date),
        PropTypes.string,
    ]),
    defaultValue: PropTypes.oneOfType([
        PropTypes.instanceOf(moment),
        PropTypes.instanceOf(Date),
        PropTypes.string,
    ]),
    /**
     * Минимальная дата
     */
    min: PropTypes.oneOfType([
        PropTypes.instanceOf(moment),
        PropTypes.instanceOf(Date),
        PropTypes.string,
    ]),
    /**
     * Максимальная дата
     */
    max: PropTypes.oneOfType([
        PropTypes.instanceOf(moment),
        PropTypes.instanceOf(Date),
        PropTypes.string,
    ]),
    /**
     * Callback изменения
     */
    onChange: PropTypes.func,
    /**
     * Формат даты
     */
    dateFormat: PropTypes.string,
    /**
     * Формат времени
     */
    timeFormat: PropTypes.string,
    /**
     * Выходной формат
     */
    outputFormat: PropTypes.string,
    /**
     * Флаг активности
     */
    disabled: PropTypes.bool,
    /**
     * Placeholder
     */
    placeholder: PropTypes.string,
    /**
     * Локализация
     */
    configLocale: PropTypes.oneOf(['en', 'ru']),
    /**
     * Флаг включения открытия при фокусе
     */
    openOnFocus: PropTypes.bool,
    dateDivider: PropTypes.string,
    className: PropTypes.string,
    config: PropTypes.any,
}

export default getContext({
    configLocale: PropTypes.string,
})(DateInterval)
