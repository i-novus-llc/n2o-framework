import React, { Component } from 'react';
import PropTypes from 'prop-types';
import moment from 'moment';
import includes from 'lodash/includes';
import isEqual from 'lodash/isEqual';
import pick from 'lodash/pick';
import values from 'lodash/values';
import join from 'lodash/join';
import map from 'lodash/map';
import defaultTo from 'lodash/defaultTo';
import TimePicker from 'rc-time-picker';

const reference = {
  hours: {
    format: 'HH',
    en: '[h]',
    ru: '[ч]',
  },
  minutes: {
    format: 'mm',
    en: '[min]',
    ru: '[мин]',
  },
  seconds: {
    format: 'ss',
    en: '[sec]',
    ru: '[сек]',
  },
};

/**
 * Компонент TimePicker
 * @reactProps {string } prefix - префикс
 * @reactProps {array} mode - режим отображения списка выбора, может быть "hours,minutes,seconds" or "hours,minutes" or "hours" or "minutes", по умолчанию "hours,minutes,seconds"
 * @reactProps {string} dataFormat - формат данных в модели, по умолчанию "hh:mm:ss"
 * @reactProps {string} format - формат отобsражения времени, может быть digit(значит "00:00:00") or symbols(значит "15 мин"), по умолчанию symbols
 * @reactProps {string} defaultValue - значение по умолчанию
 */
export class TimePickerControl extends Component {
  constructor(props) {
    super(props);
    this.state = {
      value: props.defaultValue,
    };
  }

  getTimeConfig = () => {
    const { mode } = this.props;
    return {
      showHour: includes(mode, 'hours'),
      showMinute: includes(mode, 'minutes'),
      showSecond: includes(mode, 'seconds'),
    };
  };

  onChange = value => {
    const newValue = moment(value).format(this.props.dataFormat);
    this.setState({ value: newValue }, () => this.props.onChange(newValue));
  };

  formatView = () => {
    const { format } = this.props;
    if (format === 'digit') {
      return this.props.dataFormat;
    }
    if (format === 'symbols') {
      return join(
        map(
          values(pick(reference, this.props.mode)),
          i => `${i.format} ${i[this.props.locale]}`
        ),
        ' '
      );
    }
  };

  onClose = () => {
    this.props.onClose(this.state.value);
  };

  render() {
    const { prefix, placeholder, disabled, locale, dataFormat } = this.props;
    const { value } = this.state;
    const format = this.formatView();
    const timeConfig = this.getTimeConfig();
    const readyValue = value ? moment(value, dataFormat) : null;

    return (
      <div className="time-wrapper">
        {prefix ? <span className="time-prefix">{prefix}</span> : null}
        <TimePicker
          locale={locale}
          disabled={disabled}
          placeholder={placeholder}
          format={format}
          value={readyValue}
          onChange={this.onChange}
          onClose={this.onClose}
          placement={'bottomRight'}
          {...timeConfig}
          allowEmpty={false}
          inputIcon={<span className="suffix-icon" />}
        />
      </div>
    );
  }
}

TimePickerControl.propTypes = {
  /**
   * Значение компонента
   */
  value: PropTypes.string,
  /**
   * Префикс
   */
  prefix: PropTypes.string,
  /**
   * Режим отображения попапа времени ["hours", "minutes", "seconds"]
   */
  mode: PropTypes.array,
  /**
   * Формат времени для модели
   */
  dataFormat: PropTypes.string,
  /**
   * Формат врмени для отображения "digit" || "symbols"
   */
  format: PropTypes.oneOf(['digit', 'symbols']),
  /**
   * Дефолтное значение
   */
  defaultValue: PropTypes.string,
  /**
   * onChange
   */
  onChange: PropTypes.func,
  /**
   * Локализация
   */
  locale: PropTypes.string,
  /**
   * Плэйсхолдер
   */
  placeholder: PropTypes.string,
  /**
   * On/Off
   */
  enable: PropTypes.bool,
};

TimePickerControl.defaultProps = {
  mode: ['hours', 'minutes', 'seconds'],
  format: 'symbols',
  dataFormat: 'HH:mm:ss',
  locale: 'ru',
  onChange: () => {},
  onClose: () => {},
};

export default TimePickerControl;
