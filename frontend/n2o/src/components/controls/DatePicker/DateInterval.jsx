import React from 'react';
import PropTypes from 'prop-types';
import moment from 'moment';
import { defaults } from 'lodash';
import DateTimeControl from './DateTimeControl';

/**
 * Компонент для выбора временного интервала. Состоит 2 {@link DatePicker}
 * @reactProps {object} defaultTime
 * @reactProps {array} value - массив, объекты которого задают дефолтные значения, имя ('beginDate' или 'endDate') и дефолтное время каждому пикеру.
 * @reactProps {string|moment|Date} min - самая ранняя доступная дата
 * @reactProps {string|moment|Date} max - самая поздняя дотсупная дата
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
function DateInterval({ value, defaultTime, defaultValue, onChange, ...rest }) {
  const newValue = defaults(value, defaultValue);

  const handleChange = data => {
    onChange({
      [DateTimeControl.beginInputName]: data[0],
      [DateTimeControl.endInputName]: data[1]
    });
  };

  const mappedValue = [
    {
      name: DateTimeControl.beginInputName,
      value: newValue[DateTimeControl.beginInputName],
      defaultTime
    },
    {
      name: DateTimeControl.endInputName,
      value: newValue[DateTimeControl.endInputName],
      defaultTime
    }
  ];

  return (
    <DateTimeControl {...rest} value={mappedValue} onChange={handleChange} type="date-interval" />
  );
}

DateInterval.defaultProps = {
  defaultTime: undefined,
  defaultValue: {
    [DateTimeControl.beginInputName]: null,
    [DateTimeControl.endInputName]: null
  },
  onChange: () => {},
  onFocus: () => {},
  onBlur: () => {},
  dateFormat: 'DD/MM/YYYY',
  placeholder: '',
  disabled: false,
  dateDivider: ' ',
  className: '',
  locale: 'ru',
  openOnFocus: false
};

DateInterval.propTypes = {
  onFocus: PropTypes.func,
  onBlur: PropTypes.func,
  defaultTime: PropTypes.object,
  value: PropTypes.object,
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
  onChange: PropTypes.func,
  dateFormat: PropTypes.string,
  timeFormat: PropTypes.string,
  outputFormat: PropTypes.string,
  disabled: PropTypes.bool,
  placeholder: PropTypes.string,
  locale: PropTypes.oneOf(['en', 'ru']),
  openOnFocus: PropTypes.bool
};

export default DateInterval;
