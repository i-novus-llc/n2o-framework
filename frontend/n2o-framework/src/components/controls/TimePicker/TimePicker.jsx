import React, { Component } from 'react';
import PropTypes from 'prop-types';
import moment from 'moment';
import isArray from 'lodash/isArray';
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
    if (
      isArray(mode) &&
      mode.length === 3 &&
      mode[0] === 'hours' &&
      mode[1] === 'minutes' &&
      mode[2] === 'seconds'
    ) {
      return {
        showHour: true,
        showMinute: true,
        showSecond: true,
      };
    } else if (
      isArray(mode) &&
      mode.length === 2 &&
      mode[0] === 'hours' &&
      mode[1] === 'minutes'
    ) {
      return {
        showHour: true,
        showMinute: true,
        showSecond: false,
      };
    } else if (isArray(mode) && mode.length === 1) {
      if (mode[0] === 'hours') {
        return {
          showHour: true,
          showMinute: false,
          showSecond: false,
        };
      } else {
        return {
          showHour: false,
          showMinute: true,
          showSecond: false,
        };
      }
    } else {
      return {
        showHour: true,
        showMinute: true,
        showSecond: true,
      };
    }
  };

  onChange = value => {
    const newValue = moment(value, 'HH:mm:ss');
    this.setState({ value: newValue.format(this.props.dataFormat) });
  };

  formatView = format => {
    if (format === 'digit') {
      return 'HH:mm:ss';
    } else {
      if (this.props.locale === 'ru') {
        return 'HH [ч] mm [мин] ss [сек]';
      } else {
        return 'HH [h] mm [min] ss [sec]';
      }
    }
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
    const { prefix, mode, format } = this.props;
    const { value } = this.state;
    return (
      <div className="time-wrapper">
        {prefix && prefix !== '' ? (
          <span className="time-prefix">{prefix}</span>
        ) : null}
        <TimePicker
          defaultValue={
            value === '' || !value ? moment() : moment(value, 'HH:mm:ss')
          }
          onChange={this.onChange}
          format={this.formatView(format)}
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
};

TimePickerControl.defaultProps = {
  value: '',
  mode: ['hours', 'minutes', 'seconds'],
  format: '"hh:mm:ss"',
};

export default TimePickerControl;
