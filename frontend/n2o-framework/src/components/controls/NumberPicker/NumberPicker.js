import React, { useEffect } from 'react';
import PropTypes from 'prop-types';
import cn from 'classnames';
import isNil from 'lodash/isNil';

import InputNumber from '../InputNumber/InputNumber';

import { NumberPickerButton } from './NumberPickerButton';

/**
 * Компонент - инпут с возможностью увеличения/уменьшения значения на шаг
 * @reactProps {number} value - начальное значение
 * @reactProps {boolean} visible - отображается или нет
 * @reactProps {boolean} disabled - задизейблен инпут или нет
 * @reactProps {string} step - шаг, на который увеличивается / уменьшается значение
 * @reactProps {number} min - минимальное возможное значение
 * @reactProps {number} max - максимальное возможное значение
 * @reactProps {number} onChange - выполняется при изменении значения поля
 * @reactProps {object} style - стили компонента
 * @example
 * <NumberPicker onChange={onChange}
 *             value={1}
 *             step={1}
 *             max={100}
 *             min={1}
 * />
 */

function NumberPicker(props) {
  const {
    visible,
    value,
    max,
    min,
    step,
    className,
    style,
    disabled,
    onChange,
  } = props;

  let defaultValue = 0;

  if (!isNil(min)) {
    defaultValue = min;
  } else if (!isNil(max)) {
    defaultValue = max;
  }

  useEffect(() => {
    if (value < min) {
      onChange(min);
    } else if (value > max) {
      onChange(max);
    }
  }, [value, max, min, onChange]);

  const handlerChange = step => {
    if (isNil(value) || value === '') {
      onChange(defaultValue);
    } else if (value => min && value <= max) {
      onChange(value + step);
    }
  };

  return (
    visible && (
      <div className={cn('n2o-number-picker', className)} style={style}>
        <NumberPickerButton
          disabled={disabled || min >= value}
          onClick={() => handlerChange(-step)}
        >
          <i className="fa fa-minus" aria-hidden="true" />
        </NumberPickerButton>
        <InputNumber
          className="n2o-number-picker__input"
          value={value}
          min={min}
          max={max}
          step={step}
          onChange={onChange}
          showButtons={false}
          disabled={disabled}
        />
        <NumberPickerButton
          disabled={disabled || value >= max}
          onClick={() => handlerChange(step)}
        >
          <i className="fa fa-plus" aria-hidden="true" />
        </NumberPickerButton>
      </div>
    )
  );
}

NumberPicker.defaultProps = {
  visible: true,
  style: {},
  min: 1,
  max: 100,
  step: 1,
  disabled: false,
  onChange: () => {},
};

NumberPicker.PropTypes = {
  /**
   * флаг видимости
   */
  visible: PropTypes.bool,
  /**
   * значение контрола
   */
  value: PropTypes.number,
  /**
   * максимальное значение контрола
   */
  max: PropTypes.number,
  /**
   * максимальное значение контрола
   */
  min: PropTypes.number,
  /**
   * шаг изменения значения
   */
  step: PropTypes.number,
  /**
   * класс компонента
   */
  className: PropTypes.string,
  /**
   * стили компонента
   */
  style: PropTypes.object,
  /**
   * Callback на изменение
   */
  onChange: PropTypes.func,
};

export default NumberPicker;
