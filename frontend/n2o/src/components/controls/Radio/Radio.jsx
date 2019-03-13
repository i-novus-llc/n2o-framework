import React from 'react';
import PropTypes from 'prop-types';
import { setDisplayName } from 'recompose';
import Input from '../Input/Input';
import cx from 'classnames';

/**
 * Компонент Radio
 * @reactProps {string|number} value - уникально определяет элемент
 * @reactProps {boolean} checked - начальное значение
 * @reactProps {boolean} disabled -только для чтения / нет
 * @reactProps {function} onChange - вызывается при изменении значения
 * @reactProps {string} compileLabel - лейбл
 */
class Radio extends React.Component {
  /**
   * базовый рендер
   * */
  render() {
    const { label, disabled, value, checked, onChange } = this.props;

    return (
      <div className={cx('radio', { checked })}>
        <label>
          <Input
            disabled={disabled}
            type="radio"
            value={value}
            checked={checked}
            onChange={onChange}
          />{' '}
          {this.props.label}
        </label>
      </div>
    );
  }
}

Radio.propTypes = {
  value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  checked: PropTypes.bool,
  disabled: PropTypes.bool,
  onChange: PropTypes.func,
  label: PropTypes.string
};

export default setDisplayName('Radio')(Radio);
