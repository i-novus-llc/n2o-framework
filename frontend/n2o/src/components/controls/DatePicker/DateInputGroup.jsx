import React from 'react';
import PropTypes from 'prop-types';
import moment from 'moment';

import { findDOMNode } from 'react-dom';

import DateInput from './DateInput';

/**
 * @reactProps {string} dateFormat
 * @reactProps {boolean} disabled
 * @reactProps {string} placeholder
 * @reactProps {moment} value
 * @reactProps {function} onInputChange
 * @reactProps {function} setVisibility
 */
class DateInputGroup extends React.Component {
  constructor(props) {
    super(props);
  }

  /**
   * рендер полей-инпутов
   */
  renderInputs() {
    const {
      dateFormat,
      disabled,
      placeholder,
      value,
      onInputChange,
      setVisibility,
      onFocus,
      onBlur
    } = this.props;
    const style = { display: 'flex', flexGrow: 1 };
    return (
      <div style={style}>
        {Object.keys(value).map((input, i) => {
          return (
            <DateInput
              key={i}
              dateFormat={dateFormat}
              placeholder={placeholder}
              name={input}
              disabled={disabled}
              value={value[input]}
              onInputChange={onInputChange}
              setVisibility={setVisibility}
              onFocus={onFocus}
              onBlur={onBlur}
            />
          );
        })}
      </div>
    );
  }

  /**
   * базовый рендер
   */
  render() {
    const { inputRef } = this.props;
    const style = { display: 'flex', justifyContent: 'space-between', flexGrow: 1 };
    return (
      <div ref={inputRef} style={style}>
        {this.renderInputs()}
      </div>
    );
  }
}

DateInput.propTypes = {
  dateFormat: PropTypes.string,
  disabled: PropTypes.bool,
  placeholder: PropTypes.bool,
  value: PropTypes.instanceOf(moment),
  onInputChange: PropTypes.func,
  setVisibility: PropTypes.func,
  onFocus: PropTypes.func,
  onBlur: PropTypes.func
};

DateInput.defaultProps = {
  onFocus: () => {},
  onBlur: () => {}
};

export default DateInputGroup;
