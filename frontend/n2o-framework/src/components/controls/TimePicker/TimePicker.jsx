import React, { Component } from 'react';
import PropTypes from 'prop-types';
import moment from 'moment';
import isArray from 'lodash/isArray';
import each from 'lodash/each';
import isEqual from 'lodash/isEqual';
import TimePicker from 'rc-time-picker';

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
      mode: props.mode,
      format: props.dataFormat,
    };
  }

  renderTime = mode => {
    let config;
    each(mode, () => {
      config = {
        showHour: mode[0] === 'hours' ? true : false,
        showMinute:
          (mode.length === 2 || mode.length === 3 || mode.length === 1) &&
          (mode[0] === 'minutes' ||
            mode[1] === 'minutes' ||
            mode[2] === 'minutes')
            ? true
            : false,
        showSecond:
          (mode.length === 2 || mode.length == 3 || mode.length === 1) &&
          (mode[0] === 'seconds' ||
            mode[1] === 'seconds' ||
            mode[2] === 'seconds')
            ? true
            : false,
      };
    });
    return config;
  };

  onChange = value => {
    const newValueObj = moment(value);
    const newValue = newValueObj.format(this.props.dataFormat);
    this.setState({ value: newValue }, () => this.props.onChange(newValue));
  };

  formatView = (format, use12Hours) => {
    if (format === 'digit') {
      return use12Hours ? 'hh:mm:ss A' : 'hh:mm:ss';
    } else {
      if (this.props.locale === 'ru') {
        return use12Hours
          ? 'hh [ч] mm [мин] ss [сек] A'
          : 'hh [ч] mm [мин] ss [сек]';
      } else {
        return use12Hours
          ? 'hh [h] mm [min] ss [sec] A'
          : 'hh [ч] mm [мин] ss [сек]';
      }
    }
  };

  onClose = () => {
    this.props.onClose(this.state.value);
  };

  componentDidUpdate = (prevProps, prevState) => {
    const { value, mode } = this.state;
    if (!isEqual(prevState.value, value)) {
      this.setState({ value });
    }
    if (!isEqual(prevState.mode, mode)) {
      this.setState({ mode });
    }
  };

  render() {
    const {
      prefix,
      mode,
      format,
      use12Hours,
      placeholder,
      disabled,
    } = this.props;
    const { value } = this.state;

    return (
      <div className="time-wrapper">
        {prefix && prefix !== '' ? (
          <span className="time-prefix">{prefix}</span>
        ) : null}
        <TimePicker
          locale="ru"
          disabled={disabled}
          onClose={this.onClose}
          placeholder={placeholder}
          defaultValue={
            value === '' || !value ? '' : moment(value, 'hh:mm:ss A')
          }
          use12Hours={use12Hours}
          onChange={this.onChange}
          format={this.formatView(format, use12Hours)}
          {...this.renderTime(mode)}
        />
      </div>
    );
  }
}

TimePickerControl.propTypes = {
  /**
   * Префикс
   */
  prefix: PropTypes.string,
  /**
   * Режим отображения попапа времени
   */
  mode: PropTypes.array,
  /**
   * Формат времени для модели
   */
  dataFormat: PropTypes.string,
  /**
   * Формат врмени для отображения
   */
  format: PropTypes.string,
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
   * Формат времени 12/24 часа
   */
  use12Hours: PropTypes.bool,
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
  value: '',
  mode: ['hours', 'minutes', 'seconds'],
  format: '"hh:mm:ss"',
  onChange: () => {},
  onClose: () => {},
};

export default TimePickerControl;
