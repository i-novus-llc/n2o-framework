import React from 'react';
import PropTypes from 'prop-types';
import _ from 'lodash';
import cx from 'classnames';
import { setDisplayName } from 'recompose';
import Input from '../Input/Input';

/**
 * Альтернативный радио контрол
 * @reactProps {string|number} value - уникально определяет элемент
 * @reactProps {boolean} checked - начальное значение
 * @reactProps {boolean} disabled -только для чтения / нет
 * @reactProps {function} onChange - вызывается при изменении значения
 * @reactProps {string} label - лейбл
 * @reactProps {boolean} inline - в ряд
 */

class RadioN2O extends React.Component {
  constructor(props) {
    super(props);

    this.elementId = _.uniqueId('checkbox-');
  }

  /**
   * Рендер
   */

  render() {
    const { label, disabled, value, checked, onChange, inline } = this.props;
    console.log('point');
    console.log(value);
    return (
      <div
        className={cx('custom-control custom-radio', {
          'custom-control-inline': inline,
          checked,
        })}
      >
        <Input
          id={this.elementId}
          className="custom-control-input"
          disabled={disabled}
          type="radio"
          value={value}
          checked={checked}
          onChange={onChange}
        />
        <label className="custom-control-label" htmlFor={this.elementId}>
          {label}
        </label>
      </div>
    );
  }
}

RadioN2O.propTypes = {
  value: PropTypes.any,
  checked: PropTypes.bool,
  disabled: PropTypes.bool,
  onChange: PropTypes.func,
  label: PropTypes.string,
  inline: PropTypes.bool,
};

RadioN2O.defaultProps = {
  disabled: false,
  checked: false,
  inline: false,
};

export default setDisplayName('RadioN2O')(RadioN2O);
