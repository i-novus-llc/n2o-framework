import React from 'react';
import PropTypes from 'prop-types';

import InputNumber from './InputNumber';

/**
 * Компонент - инпут для ввода интевала чисел с возможностью увеличения/уменьшения значения на шаг; состоит из 2 {@link InputNumber}
 * @reactProps {array} value - массив начальных значений
 * @reactProps {boolean} visible - отображается или нет
 * @reactProps {boolean} disabled - задизейблен инпут или нет
 * @reactProps {string} step - шаг, на который увеличивается / уменьшается значение
 * @reactProps {number} min - минимальное возможное значение
 * @reactProps {number} max - максимальное возможное значение
 * @reactProps {string} name - имя поля
 * @reactProps {number} showButtons - отображать кнопки для увеличения/уменьшения значения / не отображать
 * @reactProps {number} onChange - выполняется при изменении значения поля
 * @example
 * <InputNumberInterval onChange={this.onChange}
 *             value={1}
 *             step='0.1'
 *             name='InputNumberIntervalExample' />
 */

class InputNumberInterval extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      value: props.value,
    };
  }

  onChange(index, value) {
    this.setState(
      { value: this.state.value.map((val, i) => (i === index ? value : val)) },
      () => this.props.onChange(this.state.value)
    );
  }

  /**
   * Базовый рендер
   */
  render() {
    const { description } = this.props;
    const { value } = this.state;
    const props = { ...this.props };
    delete props.value;
    delete props.description;
    delete props.onChange;
    const style = {
      display: 'flex',
      alignItems: 'baseline',
    };
    return (
      <div>
        <div style={style}>
          <InputNumber
            value={value[0]}
            onChange={this.onChange.bind(this, 0)}
            {...props}
          />
          {'-'}
          <InputNumber
            onChange={this.onChange.bind(this, 1)}
            value={value[1]}
            {...props}
          />
        </div>
        {description && (
          <p className="n2o-number-interval-description">{description}</p>
        )}
      </div>
    );
  }
}

InputNumberInterval.defaultProps = {
  value: [1, 1],
  disabled: false,
  visible: true,
  step: '0.1',
  showButtons: true,
  description: '',
  onChange: val => {},
};

InputNumberInterval.propTypes = {
  /**
   * Значение [1, 2]
   */
  value: PropTypes.array,
  /**
   * Флаг видимости
   */
  visible: PropTypes.bool,
  /**
   * Флаг активности
   */
  disabled: PropTypes.bool,
  /**
   * Шаг кнопки
   */
  step: PropTypes.string,
  /**
   * Минимальное число
   */
  min: PropTypes.number,
  /**
   * Масимальное число
   */
  max: PropTypes.number,
  /**
   * Название контрола
   */
  name: PropTypes.string,
  /**
   * Флаг показа кнопок
   */
  showButtons: PropTypes.bool,
  /**
   * Callback изменения
   */
  onChange: PropTypes.func,
};

export default InputNumberInterval;
