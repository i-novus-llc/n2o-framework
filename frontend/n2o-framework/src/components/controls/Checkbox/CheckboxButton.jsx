import React from 'react';
import PropTypes from 'prop-types';
import cx from 'classnames';
import { isNil } from 'lodash';
import { setDisplayName } from 'recompose';
import Input from '../Input/Input';

/**
 * Компонент чекбоксов в виде кнопок
 * @reactProps {string|number} value - уникально определяет элемент
 * @reactProps {boolean} checked - начальное значение
 * @reactProps {boolean} disabled - только для чтения / нет
 * @reactProps {function} onChange - вызывается при изменении значения
 * @reactProps {string} label - лейбл
 */

class CheckboxButton extends React.Component {
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
      <label
        className={cx('btn btn-secondary', {
          active: checked,
          disabled: disabled,
        })}
      >
        <Input
          disabled={disabled}
          type="checkbox"
          value={value}
          checked={isNil(checked) ? !!value : checked}
          onChange={onChange}
          onFocus={onFocus}
          onBlur={onBlur}
        />
        {label}
      </label>
    );
  }
}

CheckboxButton.propTypes = {
  value: PropTypes.any,
  checked: PropTypes.bool,
  onChange: PropTypes.func,
  disabled: PropTypes.bool,
  label: PropTypes.string,
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

CheckboxButton.defaultProps = {
  checked: false,
  disabled: false,
  onFocus: () => {},
  onBlur: () => {},
  onChange: () => {},
  onPaste: () => {},
  onClick: () => {},
  onKeyDown: () => {},
};

export default setDisplayName('CheckboxButton')(CheckboxButton);
