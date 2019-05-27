import React from 'react';
import PropTypes from 'prop-types';
import cn from 'classnames';
import { toNumber, toString, isNil, isNaN, isEqual } from 'lodash';

import Input from '../Input/Input';

import {
  formatToFloat,
  isValid,
  matchesWhiteList,
  getPrecision,
} from './utils';

/**
 * Компонент - инпут для ввода чисел с возможностью увеличения/уменьшения значения на шаг
 * @reactProps {number} value - начальное значение
 * @reactProps {boolean} visible - отображается или нет
 * @reactProps {boolean} disabled - задизейблен инпут или нет
 * @reactProps {string} step - шаг, на который увеличивается / уменьшается значение
 * @reactProps {number} min - минимальное возможное значение
 * @reactProps {number} max - максимальное возможное значение
 * @reactProps {string} name - имя поля
 * @reactProps {number} showButtons - отображать кнопки для увеличения/уменьшения значения / не отображать
 * @reactProps {number} onChange - выполняется при изменении значения поля
 * @example
 * <InputNumber onChange={this.onChange}
 *             value={1}
 *             step='0.1'
 *             name='InputNumberExample' />
 */
class InputNumber extends React.Component {
  constructor(props) {
    super(props);
    const value = props.value;
    this.precision = getPrecision(props.step);
    this.pasted = false;
    this.state = {
      value:
        !isNil(value) && !isNaN(toNumber(value)) && value !== ''
          ? toNumber(value).toFixed(this.precision)
          : null,
    };
    this.onChange = this.onChange.bind(this);
    this.onPaste = this.onPaste.bind(this);
    this.onKeyDown = this.onKeyDown.bind(this);
    this.onBlur = this.onBlur.bind(this);
  }

  componentDidUpdate(prevProps) {
    const { value } = this.props;
    if (prevProps.value !== value && !isNil(value)) {
      this.setState({ value });
    } else if (
      !isEqual(prevProps.value, value) &&
      (value === '' || isNil(value))
    ) {
      this.setState({ value: null });
    }
  }

  /**
   * Обработчик вставки
   * @param e
   */
  onPaste(e) {
    this.pasted = true;
    this.setState({ value: this.inputElement.value });
  }

  onChange(value) {
    const nextValue = value === '' ? null : toNumber(value);
    const { max, min } = this.props;
    if (isNil(nextValue)) {
      this.setState({ value: null }, () => this.props.onChange(null));
    }
    if (!isValid(nextValue, min, max)) {
      return;
    }
    if (matchesWhiteList(nextValue) || this.pasted) {
      this.setState({ value }, () => this.props.onChange(nextValue));
    }
  }

  /**
   * Обрабатывает изменение значения с клавиатуры
   * @param type {string} - 'up' (увеличение значения) или 'down' (уменьшение значения)
   */
  buttonHandler(type) {
    const { min, max, step } = this.props;
    const { value } = this.state;
    const delta = toNumber(formatToFloat(step, this.precision));
    const val =
      !isNil(value) && value !== ''
        ? toNumber(value).toFixed(this.precision)
        : null;
    const currentValue = toNumber(formatToFloat(val, this.precision));
    let newValue = currentValue;
    if (type === 'up') {
      newValue = currentValue + delta;
    } else if (type === 'down') {
      newValue = currentValue - delta;
    }
    if (isValid(newValue, min, max)) {
      this.setState({ value: newValue.toFixed(this.precision) }, () =>
        this.props.onChange(newValue)
      );
    }
  }

  onBlur(e) {
    const { max, min } = this.props;
    const value = formatToFloat(this.state.value, this.precision);
    this.pasted = false;
    if (!isNil(value) && isValid(value, min, max)) {
      this.setState({ value });
    } else {
      this.setState({ value: null });
    }
    this.props.onBlur();
  }

  /**
   * Вызывает buttonHandler с нужным аргументом (в зависимости от нажатой клавиши)
   * @param e
   */
  onKeyDown(e) {
    const upKeyCode = 38;
    const downKeyCode = 40;
    const type =
      e.keyCode === upKeyCode
        ? 'up'
        : e.keyCode === downKeyCode
        ? 'down'
        : undefined;
    if (type) {
      e.preventDefault();
      this.buttonHandler(type);
    }
  }

  /**
   * Базовый рендер
   * */
  render() {
    const {
      visible,
      disabled,
      name,
      step,
      min,
      max,
      showButtons,
      className,
      onFocus,
      autoFocus,
    } = this.props;
    const { value } = this.state;

    return (
      visible && (
        <div
          className="n2o-input-number"
          ref={input => {
            this.input = input;
          }}
        >
          <Input
            onKeyDown={this.onKeyDown}
            name={name}
            value={toString(value)}
            step={step}
            min={min}
            max={max}
            className={cn(['form-control', { [className]: className }])}
            onBlur={this.onBlur}
            onFocus={onFocus}
            onChange={({ target }) => this.onChange(target.value)}
            onPaste={this.onPaste}
            disabled={disabled}
            autoFocus={autoFocus}
          />
          {showButtons && (
            <div className="n2o-input-number-buttons">
              <button
                onClick={this.buttonHandler.bind(this, 'up')}
                disabled={disabled}
                tabIndex={-1}
              >
                <i className="fa fa-angle-up" aria-hidden="true" />
              </button>
              <button
                onClick={this.buttonHandler.bind(this, 'down')}
                disabled={disabled}
                tabIndex={-1}
              >
                <i className="fa fa-angle-down" aria-hidden="true" />
              </button>
            </div>
          )}
        </div>
      )
    );
  }
}

InputNumber.defaultProps = {
  disabled: false,
  visible: true,
  step: '0.1',
  autoFocus: false,
  showButtons: true,
  onChange: val => {},
  onBlur: val => {},
  onFocus: val => {},
};

InputNumber.propTypes = {
  value: PropTypes.number,
  visible: PropTypes.bool,
  disabled: PropTypes.bool,
  step: PropTypes.string,
  min: PropTypes.number,
  max: PropTypes.number,
  name: PropTypes.string,
  showButtons: PropTypes.bool,
  onChange: PropTypes.func,
  className: PropTypes.string,
  autoFocus: PropTypes.bool,
};

export default InputNumber;
