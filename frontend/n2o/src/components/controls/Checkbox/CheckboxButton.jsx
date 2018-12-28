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
    const { label, disabled, value, checked, onChange } = this.props;

    return (
      <label className={cx('btn btn-secondary', { active: checked, disabled: disabled })}>
        <Input
          disabled={disabled}
          type="checkbox"
          value={value}
          checked={isNil(checked) ? !!value : checked}
          onChange={onChange}
        />
        {label}
      </label>
    );
  }
}

CheckboxButton.propTypes = {
  value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  checked: PropTypes.bool,
  onChange: PropTypes.func,
  disabled: PropTypes.bool,
  label: PropTypes.string
};

CheckboxButton.defaultProps = {
  checked: false,
  disabled: false
};

export default setDisplayName('CheckboxButton')(CheckboxButton);
