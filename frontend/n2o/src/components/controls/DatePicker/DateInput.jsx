import React from 'react';
import PropTypes from 'prop-types';
import moment from 'moment';
import cx from 'classnames';

import DateTimeControl from './DateTimeControl';

/**
 * Компонент DateInput
 * @reactProps {string} dateFormat
 * @reactProps {string} defaultTime
 * @reactProps {string} placeholder
 * @reactProps {boolean} disabled
 * @reactProps {moment} value
 * @reactProps {function} inputOnClick
 * @reactProps {string} inputClassName
 * @reactProps {string} name
 * @reactProps {function} onClick
 */
class DateInput extends React.Component {
  constructor(props) {
    super(props);
    const { value, dateFormat } = props;
    this.state = {
      value: value && value.format(dateFormat)
    };
    this.onChange = this.onChange.bind(this);
    this.onFocus = this.onFocus.bind(this);
    this.onButtonClick = this.onButtonClick.bind(this);
  }

  onChange(e) {
    const { value } = e.target;
    const { dateFormat, name } = this.props;
    if (value === '') {
      this.props.onInputChange(null, name);
    } else if (moment(value, dateFormat).format(dateFormat) === value) {
      this.props.onInputChange(moment(value, dateFormat), name);
    } else {
      this.setState({ value });
    }
  }

  onFocus(e) {
    const { setVisibility, onFocus } = this.props;
    setVisibility(true);
    onFocus && onFocus(e);
  }

  /**
   * Показывается попап при нажатии на кнопку с иконкой календаря
   */
  onButtonClick() {
    this.props.setVisibility(true);
  }

  /**
   * Базовый рендер
   */
  render() {
    const { onClick, disabled, placeholder, name } = this.props;
    const inputStyle = { flexGrow: 1 };
    const dashStyle = { alignSelf: 'center' };
    return (
      <div
        className={cx('n2o-date-input', {
          'n2o-date-input-first': name === DateTimeControl.beginInputName,
          'n2o-date-input-last': name === DateTimeControl.endInputName
        })}
      >
        {name === DateTimeControl.endInputName && <span style={dashStyle}>-</span>}
        <input
          type="text"
          className="form-control"
          placeholder={placeholder}
          disabled={disabled}
          value={this.state.value}
          onChange={this.onChange}
          onClick={onClick}
          style={inputStyle}
          onFocus={this.onFocus}
          onBlur={this.props.onBlur}
        />
        {(name === DateTimeControl.defaultInputName || name === DateTimeControl.endInputName) && (
          <button
            disabled={disabled}
            onClick={this.onButtonClick}
            className="btn n2o-calendar-button"
          >
            <i className="fa fa-calendar" aria-hidden="true" />
          </button>
        )}
      </div>
    );
  }

  componentWillReceiveProps(props) {
    const { value, dateFormat } = props;
    if (props.value) {
      this.setState({ value: value.format(dateFormat) });
    } else {
      this.setState({ value: '' });
    }
  }
}

DateInput.defaultProps = {
  value: moment(),
  dateFormat: 'DD/MM/YYYY',
  onFocus: () => {},
  onBlur: () => {}
};

DateInput.propTypes = {
  onFocus: PropTypes.func,
  onBlur: PropTypes.func,
  dateFormat: PropTypes.string,
  defaultTime: PropTypes.string,
  placeholder: PropTypes.string,
  disabled: PropTypes.bool,
  value: PropTypes.instanceOf(moment),
  inputOnClick: PropTypes.func,
  inputClassName: PropTypes.string,
  name: PropTypes.string,
  onClick: PropTypes.func
};
export default DateInput;
