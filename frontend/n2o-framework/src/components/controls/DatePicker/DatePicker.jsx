import React from 'react'
import PropTypes from 'prop-types'
import moment from 'moment'
import { getContext } from 'recompose'

import DateTimeControl from './DateTimeControl'

/**
 * Компонент для выбора даты. Состоит из поля ввода {@link DateInput} и попапа {@link PopUp} с календарем {@link Calendar}.
 * @reactProps {string} defaultTime - время выбранной даты. Пример: '11:11'
 * @reactProps {string|moment|Date} date - дефолтная дата.
 * @reactProps {string|moment|Date} min - самая ранняя доступная дата
 * @reactProps {string|moment|Date} max - самая поздняя доступная дата
 * @reactProps {function} onChange - вызывается при изменении
 * @reactProps {string} dateFormat - формат даты. Пример: "DD-MM_YYYY"
 * @reactProps {string} timeFormat - формат времени. Пример: "HH:mm"
 * @reactProps {string} outputFormat - формат даты при сохранение. Пример: "DD-MM_YYYY HH~mm"
 * @reactProps {boolean} disabled - задизейблен пикер / нет
 * @reactProps {string} placeholder - плэйсхолдер для поля
 * @reactProps {string} locale - Локаль. Варианты: 'en', 'ru'
 * @reactProps {bool} openOnFocus - открывать календарь при фокусе
 * @example
 * <DatePicker defaultTime = '12:11'/>
 */
class DatePicker extends React.Component {
    constructor(props) {
        super(props)
    }

    /**
   * базовый рендер
   * */
    render() {
        let { value, defaultValue } = this.props
        value = value || defaultValue || null
        return <DateTimeControl {...this.props} value={value} type="date-picker" />
    }
}

DatePicker.defaultProps = {
    onChange: () => {},
    onFocus: () => {},
    onBlur: () => {},
    t: () => {},
    dateFormat: 'DD/MM/YYYY',
    configLocale: 'ru',
    placeholder: '',
    disabled: false,
    className: '',
    defaultTime: '00:00',
    autoFocus: false,
    openOnFocus: false,
}

DatePicker.propTypes = {
    /**
   * Значение
   */
    value: PropTypes.oneOfType([
        PropTypes.instanceOf(moment),
        PropTypes.instanceOf(Date),
        PropTypes.string,
    ]),
    /**
   * Минимальная дата ввода
   */
    min: PropTypes.oneOfType([
        PropTypes.instanceOf(moment),
        PropTypes.instanceOf(Date),
        PropTypes.string,
    ]),
    /**
   * Максимальная дата ввода
   */
    max: PropTypes.oneOfType([
        PropTypes.instanceOf(moment),
        PropTypes.instanceOf(Date),
        PropTypes.string,
    ]),
    /**
   * Callback фокуса
   */
    onFocus: PropTypes.func,
    /**
   * Callback потери фокуса
   */
    onBlur: PropTypes.func,
    /**
   *
   */
    dateDivider: PropTypes.string,
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
   * Выходной фомат
   */
    outputFormat: PropTypes.string,
    /**
   * Флаг активности
   */
    disabled: PropTypes.bool,
    placeholder: PropTypes.string,
    /**
   * Placeholder
   */
    configLocale: PropTypes.oneOf(['en', 'ru']),
    /**
   * Автофокус на контроле
   */
    autoFocus: PropTypes.bool,
    /**
   * Флаг открытия выбора даты при фокусе
   */
    openOnFocus: PropTypes.bool,
}

export default getContext({
    configLocale: PropTypes.string,
})(DatePicker)
