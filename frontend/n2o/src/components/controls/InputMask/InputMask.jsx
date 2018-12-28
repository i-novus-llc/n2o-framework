import React from 'react';
import PropTypes from 'prop-types';
import MaskedInput from 'react-text-mask';
import cn from 'classnames';
import createNumberMask from 'text-mask-addons/dist/createNumberMask';

/**
 * Компонент интерфейса разбивки по страницам
 * @reactProps {string} className - кастомный css-клсасс
 * @reactProps {string} preset - пресет для маски. Варианты: phone(телефон), post-code(почтовый индекс), date(дата), money(деньги), percentage(процент), card (кредитная карта)
 * @reactProps {string|array|function} mask - маска. Стандартная конфигурация: 9 - цифра, S - английская буква, Б - русская буква. Дополнительную конфигурациюю можно осуществить, используя проперти dictionary
 * @reactProps {function} onChange - выполняется при изменении значения поля
 * @reactProps {function} onComplete - выполняется после обновления значения в стейте, если значение '' или соответствует маске
 * @reactProps {string} placeholder - плэйсходер для поля
 * @reactProps {string} placeholderChar - символ, который будет на месте незаполненного символа маски
 * @reactProps {string} value - максимальное кол-во кнопок перехода между страницами
 * @reactProps {number} dictionary - дополнительные символы-ключи для маски
 * @reactProps {boolean} guide - @see https://github.com/text-mask/text-mask/blob/master/componentDocumentation.md#guide
 * @reactProps {boolean} keepCharPositions - @see https://github.com/text-mask/text-mask/blob/master/componentDocumentation.md#keepcharpositions
 * @reactProps {boolean} resetOnNotValid - сбрасывать / оставлять невалижное значение при потере фокуса
 * @example
 * <InputMask onComplete={this.onComplete}
 *             mask="99 x 99"
 *             dictionary={{x: \[01]\}}
 *             placeholderChar='?'
 *             />
 */
class InputMask extends React.Component {
  constructor(props) {
    super(props);
    this.state = { value: props.value, guide: false };
    this.valid = false;
    this.dict = {
      '9': /\d/,
      S: /[A-Za-z]/,
      Б: /[А-Яа-я]/,
      ...props.dictionary
    };
  }

  /**
   * преобразует маску-функцию, маску-строку в массив-маску (с regexp вместо символов) при помощи _mapToArray
   * @returns (number) возвращает массив-маску
   */
  mask() {
    const { mask } = this.props;
    if (Array.isArray(mask)) {
      return mask;
    } else if (typeof mask === 'function') {
      return mask();
    }
    return this._mapToArray(mask);
  }

  /**
   * возвращает маку для пресета
   * @returns (number) возвращает массив-маску для пресета-аргумента
   */
  preset(preset) {
    switch (preset) {
      case 'phone':
        return this._mapToArray('+9 (999)-999-99-99');
      case 'post-code':
        return this._mapToArray('999999');
      case 'date':
        return this._mapToArray('99.99.9999');
      case 'money':
        return createNumberMask({ prefix: '', suffix: 'Р' });
      case 'percentage':
        return createNumberMask({ prefix: '', suffix: '%' });
      case 'card':
        return this._mapToArray('9999 9999 9999 9999');
    }
  }
  /**
   * возвращает индекс первого символа маски, который еще не заполнен
   * @returns (number) индекс первого символа маски, который еще не заполнен
   */
  _indexOfFirstPlaceHolder(value) {
    return value.indexOf(this.props.placeholderChar);
  }

  /**
   * возвращает индекс последнего символа маски, который еще не заполнен
   * @returns (number) индекс последнего символа маски, который еще не заполнен
   */
  _indexOfLastPlaceholder(mask) {
    if (typeof mask === 'function') {
      return mask()
        .map(item => item instanceof RegExp)
        .lastIndexOf(true);
    } else if (typeof mask === 'string') {
      return Math.max(...Object.keys(this.dict).map(char => mask.lastIndexOf(char)));
    } else if (Array.isArray(mask)) {
      return mask.map(item => item instanceof RegExp).lastIndexOf(true);
    }
    return -1;
  }
  /**
   * проверка на валидность (соответсвие маске)
   */
  _isValid(value) {
    const { preset, mask, guide } = this.props;
    if (guide) {
      return value && this._indexOfFirstPlaceHolder(value) === -1;
    }
    return value.length > this._indexOfLastPlaceholder(this.preset(preset) || mask);
  }

  /**
   * преобразование строки маски в массив ( уже  с регулярными выражениями)
   */
  _mapToArray(mask) {
    return mask.split('').map(char => {
      return this.dict[char] ? this.dict[char] : char;
    });
  }

  _onChange(e) {
    const { value } = e.target;
    this.valid = this._isValid(value);
    this.props.onChange(value);
    this.setState({ value }, () => (this.valid || value === '') && this.props.onComplete(value));
  }

  _onBlur(e) {
    const { resetOnNotValid } = this.props;
    const { value } = this.state;
    this.valid = this._isValid(value);
    if (!this.valid) {
      const newValue = resetOnNotValid ? '' : value;
      this.setState({ value: newValue, guide: false }, () => this.props.onComplete(newValue));
    }
  }

  _onFocus() {
    if (!this.valid) {
      this.setState({ guide: this.props.guide });
    }
  }

  getDerivedStateFromProps(props, state) {
    if (props.value !== state.value) {
      return {
        ...state,
        value: props.value
      };
    }
    return null;
  }

  /**
   * обработка новых пропсов
   */
  componentDidUpdate(props) {
    this.dict = { ...this.dict, ...this.props.dictionary };
    this.valid = this._isValid(props.value);
  }

  /**
   * базовый рендер компонента
   */
  render() {
    const { preset, placeholderChar, placeholder, className } = this.props;
    const mask = this.preset(preset);
    return (
      <MaskedInput
        className={cn(['form-control', { [className]: className }])}
        placeholderChar={placeholderChar}
        placeholder={placeholder}
        guide={this.state.guide}
        mask={mask || this.mask.bind(this)}
        value={this.state.value}
        onBlur={this._onBlur.bind(this)}
        onChange={this._onChange.bind(this)}
        onFocus={this._onFocus.bind(this)}
        keepCharPositions={this.props.keepCharPositions}
      />
    );
  }
}

InputMask.defaultProps = {
  onChange: v => {},
  onComplete: v => {},
  placeholderChar: '_',
  guide: true,
  keepCharPositions: false,
  resetOnNotValid: true,
  value: '',
  dictionary: {},
  mask: ''
};

InputMask.propTypes = {
  className: PropTypes.string,
  preset: PropTypes.string,
  mask: PropTypes.oneOfType([PropTypes.string, PropTypes.array, PropTypes.func]),
  onChange: PropTypes.func,
  onComplete: PropTypes.func,
  placeholder: PropTypes.string,
  placeholderChar: PropTypes.string,
  value: PropTypes.string,
  dictionary: PropTypes.object,
  guide: PropTypes.bool,
  keepCharPositions: PropTypes.bool,
  resetOnNotValid: PropTypes.bool
};

export default InputMask;
