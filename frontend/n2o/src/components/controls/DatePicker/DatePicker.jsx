import React from 'react';
import PropTypes from 'prop-types';
import moment from 'moment';

import DateTimeControl from './DateTimeControl';

/**
 * Компонент для выбора даты. Состоит из поля ввода {@link DateInput} и попапа {@link PopUp} с календарем {@link Calendar}.
 * @reactProps {string} defaultTime - время выбранной даты. Пример: '11:11'
 * @reactProps {string|moment|Date} date - дефолтная дата.
 * @reactProps {string|moment|Date} min - самая ранняя доступная дата
 * @reactProps {string|moment|Date} max - самая поздняя дотсупная дата
 * @reactProps {function} onChange - вызывается при изменении
 * @reactProps {string} dateFormat - формат даты. Пример: "DD-MM_YYYY"
 * @reactProps {string} timeFormat - формат времени. Пример: "HH:mm"
 * @reactProps {string} outputFormat - формат даты при сохранение. Пример: "DD-MM_YYYY HH~mm"
 * @reactProps {boolean} disabled - задизейблен пикер / нет
 * @reactProps {string} placeholder - плэйсхолдер для поля
 * @reactProps {string} locale - Локаль. Варианты: 'en', 'ru'
 * @example
 * <DatePicker defaultTime = '12:11'/>
 */
class DatePicker extends React.Component {
  constructor(props) {
    super(props);
  }
  /**
   * базовый рендер
   **/
  render() {
    let { value, defaultValue } = this.props;
    value = value || defaultValue || null;
    return <DateTimeControl {...this.props} value={value} type="date-picker" />;
  }
}

DatePicker.defaultProps = {
  onChange: () => {},
  dateFormat: 'DD/MM/YYYY',
  locale: 'ru',
  placeholder: '',
  disabled: false,
  className: '',
  defaultTime: '00:00'
};

DatePicker.propTypes = {
  value: PropTypes.oneOfType([
    PropTypes.instanceOf(moment),
    PropTypes.instanceOf(Date),
    PropTypes.string
  ]),
  min: PropTypes.oneOfType([
    PropTypes.instanceOf(moment),
    PropTypes.instanceOf(Date),
    PropTypes.string
  ]),
  max: PropTypes.oneOfType([
    PropTypes.instanceOf(moment),
    PropTypes.instanceOf(Date),
    PropTypes.string
  ]),
  dateDivider: PropTypes.string,
  onChange: PropTypes.func,
  dateFormat: PropTypes.string,
  timeFormat: PropTypes.string,
  outputFormat: PropTypes.string,
  disabled: PropTypes.bool,
  placeholder: PropTypes.string,
  locale: PropTypes.oneOf(['en', 'ru'])
};

export default DatePicker;
