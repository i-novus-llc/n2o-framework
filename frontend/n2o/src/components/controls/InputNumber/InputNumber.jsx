import React from 'react';
import PropTypes from 'prop-types';
import cn from 'classnames';
import { findDOMNode } from 'react-dom';

import Input from '../Input/Input';

import { formatToFloat, isValid, matchesWhiteList, getPrecision } from './utils';

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
    const value = props.value ? +props.value : props.value === '0' ? 0 : null;
    this.precision = getPrecision(props.step);
    this.pasted = false;
    this.state = {
      value: value !== null ? +value.toFixed(this.precision) : null
    };
    this.onChange = this.onChange.bind(this);
    this.onPaste = this.onPaste.bind(this);
    this.onKeyDown = this.onKeyDown.bind(this);
    this.onBlur = this.onBlur.bind(this);
  }

  componentWillReceiveProps(props) {
    if (
      (props.value || props.value === 0) &&
      isValid(props.value, this.props.min, this.props.max)
    ) {
      this.setState({ value: props.value });
    }
    if (props.step !== this.props.step) {
      this.precision = getPrecision(props.step);
      this.setState({ value: formatToFloat(props.value, this.precision) });
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
    const { step, max, min, index } = this.props;
    if (value < min || value > max) {
      return;
    }
    if (matchesWhiteList(value) || this.pasted) {
      this.setState(
        { value },
        isValid(value, min, max) ? () => this.props.onChange(value) : undefined
      );
    }
  }

  /**
   * Обрабатывает изменение значения с клавиатуры
   * @param type {string} - 'up' (увеличение значения) или 'down' (уменьшение значения)
   */
  buttonHandler(type) {
    const { min, max, step, index } = this.props;
    const delta = Number(formatToFloat(step, this.precision));
    const val =
      this.state.value === '' ? this.props.value || this.props.min || 0 : this.state.value;
    const value = Number(formatToFloat(val, this.precision));
    let newValue = value;
    if (type === 'up') {
      newValue = value + delta;
    } else if (type === 'down') {
      newValue = value - delta;
    }
    if (isValid(value, min, max) && isValid(newValue, min, max)) {
      this.setState({ value: newValue.toFixed(this.precision) }, () =>
        this.props.onChange(this.state.value)
      );
    }
  }

  onBlur(e) {
    const { max, min, step } = this.props;
    const value = formatToFloat(this.state.value, this.precision);
    this.pasted = false;
    if (this.state.value !== '' && isValid(Number(value), min, max)) {
      this.setState({ value });
    } else {
      this.setState({ value: '' });
    }
    this.props.onBlur(e);
  }

  /**
   * Вызывает buttonHandler с нужным аргументом (в зависимости от нажатой клавиши)
   * @param e
   */
  onKeyDown(e) {
    const upKeyCode = 38;
    const downKeyCode = 40;
    const type = e.keyCode === upKeyCode ? 'up' : e.keyCode === downKeyCode ? 'down' : undefined;
    if (type) {
      e.preventDefault();
      this.buttonHandler(type);
    }
  }

  /**
   * Базовый рендер
   * */
  render() {
    const { visible, disabled, name, step, min, max, showButtons, className, onFocus } = this.props;
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
            value={value || ''}
            step={step}
            min={min}
            max={max}
            className={cn(['form-control', { [className]: className }])}
            onBlur={this.onBlur}
            onFocus={onFocus}
            onChange={({ target }) => this.onChange(target.value)}
            onPaste={this.onPaste}
            disabled={disabled}
          />
          {showButtons && (
            <div className="n2o-input-number-buttons">
              <button onClick={this.buttonHandler.bind(this, 'up')} disabled={disabled}>
                <i className="fa fa-angle-up" aria-hidden="true" />
              </button>
              <button onClick={this.buttonHandler.bind(this, 'down')} disabled={disabled}>
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
  showButtons: true,
  onChange: val => {},
  onBlur: val => {},
  onFocus: val => {}
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
  className: PropTypes.string
};

export default InputNumber;
