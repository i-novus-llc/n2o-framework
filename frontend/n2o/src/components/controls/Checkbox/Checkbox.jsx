import React from 'react';
import PropTypes from 'prop-types';
import { isNil } from 'lodash';
import { setDisplayName } from 'recompose';
import Input from '../Input/Input';

/**
 * Компонент Checkbox
 * @reactProps {string|number} value - уникально определяет элемент
 * @reactProps {boolean} checked - начальное значение
 * @reactProps {boolean} disabled -только для чтения / нет
 * @reactProps {function} onChange - вызывается при изменении значения
 * @reactProps {string} label - лейбл
 */

class Checkbox extends React.Component {
  /**
   * базовый рендер
   * */
  render() {
    const { label, disabled, value, checked, onChange } = this.props;
    return (
      <div className="checkbox">
        <label>
          <Input
            type="checkbox"
            disabled={disabled}
            value={value}
            checked={isNil(checked) ? !!value : checked}
            onChange={onChange}
          />{' '}
          {label}
        </label>
      </div>
    );
  }
}

Checkbox.propTypes = {
  value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  onChange: PropTypes.func,
  disabled: PropTypes.bool,
  label: PropTypes.string,
  checked: PropTypes.bool
};

Checkbox.defaultProps = {
  disabled: false
};

export default setDisplayName('Checkbox')(Checkbox);
