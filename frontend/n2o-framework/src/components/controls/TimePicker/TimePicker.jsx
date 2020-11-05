import React, { Component } from 'react';
import PropTypes from 'prop-types';
import moment from 'moment';
import isArray from 'lodash/isArray';
import TimePicker from 'rc-time-picker';

/**
 * Компонент TimePicker
 * @reactProps {string } prefix - префикс
 * @reactProps {array} mode - режим отображения списка выбора, может быть "hours,minutes,seconds" or "hours,minutes" or "hours" or "minutes", по умолчанию "hours,minutes,seconds"
 * @reactProps {string} dataFormat - формат данных в модели, по умолчанию "hh:mm:ss"
 * @reactProps {string} format - формат отображения времени, может быть digit(значит "00:00:00") or symbols(значит "15 мин"), по умолчанию symbols
 * @reactProps {string} defaultValue - значение по умолчанию
 */

export class TimePickerControl extends Component {
  constructor(props) {
    super(props);
    this.state = {
      value: props.defaultValue,
      mode: props.mode,
      open: false,
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
    this.setState({ value });
  };

  onOpen = () => {
    this.setState({ open: true });
  };

  onClose = () => {
    this.setState({ open: false });
  };

  prefixTime = mode =>
    mode.length === 3 ? (
      <div className="valtime">
        <span>часы</span>
        <span>мин</span>
        <span>сек</span>
      </div>
    ) : mode.length === 2 ? (
      <div className="valtime">
        <span>часы</span>
        <span>мин</span>
      </div>
    ) : mode.length === 1 && mode[0] === 'hours' ? (
      <div className="valtime">
        <span>часы</span>
      </div>
    ) : mode.length === 1 && mode[0] === 'minutes' ? (
      <div className="valtime">
        <span>мин</span>
      </div>
    ) : (
      <div className="valtime">
        <span>часы</span>
        <span>мин</span>
        <span>сек</span>
      </div>
    );

  formatView = format => {
    if (format === 'digit') {
      return 'hh:mm:ss';
    } else {
      return moment.utc().format('hh [hrs]:mm [mins]');
    }
  };

  render() {
    console.log(this.props, 'this');
    const { prefix, mode, format } = this.props;
    const { open } = this.state;
    return (
      <div className="time-wrapper">
        {prefix && prefix !== '' ? (
          <span className="time-prefix">{prefix}</span>
        ) : null}
        <TimePicker
          onOpen={this.onOpen}
          onClose={this.onClose}
          showSecond={false}
          value={moment(this.state.value, 'hh:mm:ss')}
          onChange={this.onChange}
          format={this.formatView(format)}
          {...this.renderTime(mode)}
        />
        {open && this.prefixTime(mode)}
      </div>
    );
  }
}

TimePickerControl.propTypes = {
  prefix: PropTypes.string,
  mode: PropTypes.array,
  dataFormat: PropTypes.string,
  format: PropTypes.string,
  defaultValue: PropTypes.string,
};

TimePickerControl.defaultProps = {
  prefix: '',
  mode: ['hours', 'minutes', 'seconds'],
  dataFormat: '"hh:mm:ss"',
  format: 'digit',
  defaultValue: '12:23:12',
};

export default TimePickerControl;
