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
    const {
      label,
      disabled,
      value,
      checked,
      onChange,
      onFocus,
      onBlur,
    } = this.props;
    return (
      <div className="checkbox">
        <label>
          <Input
            type="checkbox"
            disabled={disabled}
            value={value}
            checked={isNil(checked) ? !!value : checked}
            onChange={onChange}
            onFocus={onFocus}
            onBlur={onBlur}
          />{' '}
          {label}
        </label>
      </div>
    );
  }
}

Checkbox.propTypes = {
  value: PropTypes.any,
  onChange: PropTypes.func,
  disabled: PropTypes.bool,
  label: PropTypes.string,
  checked: PropTypes.bool,
  obBlur: PropTypes.func,
  className: PropTypes.string,
  style: PropTypes.object,
  placeholder: PropTypes.string,
  length: PropTypes.string,
  id: PropTypes.string,
  name: PropTypes.string,
  type: PropTypes.string,
  autoFocus: PropTypes.bool,
  onFocus: PropTypes.func,
  onBlur: PropTypes.func,
  onPaste: PropTypes.func,
  onClick: PropTypes.func,
  onKeyDown: PropTypes.func,
};

Checkbox.defaultProps = {
  disabled: false,
  onFocus: () => {},
  onBlur: () => {},
  onChange: () => {},
  onPaste: () => {},
  onClick: () => {},
  onKeyDown: () => {},
};

export default setDisplayName('Checkbox')(Checkbox);
