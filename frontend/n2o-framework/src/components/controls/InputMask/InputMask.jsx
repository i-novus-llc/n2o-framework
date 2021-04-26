import React from 'react'
import PropTypes from 'prop-types'
import MaskedInput from 'react-text-mask'
import cn from 'classnames'
import isEqual from 'lodash/isEqual'
import omit from 'lodash/omit'
import createNumberMask from 'text-mask-addons/dist/createNumberMask'
import { compose } from 'recompose'

import withRightPlaceholder from '../withRightPlaceholder'

/**
 * Компонент интерфейса разбивки по страницам
 * @reactProps {string} className - кастомный css-клсасс
 * @reactProps {string} preset - пресет для маски. Варианты: phone(телефон), post-code(почтовый индекс), date(дата), money(деньги), percentage(процент), card (кредитная карта)
 * @reactProps {string|array|function} mask - маска. Стандартная конфигурация: 9 - цифра, S - английская буква, Б - русская буква. Дополнительную конфигурациюю можно осуществить, используя проперти dictionary
 * @reactProps {function} onChange - выполняется при изменении значения поля
 * @reactProps {string} placeholder - плэйсходер для поля
 * @reactProps {string} placeholderChar - символ, который будет на месте незаполненного символа маски
 * @reactProps {string} value - максимальное кол-во кнопок перехода между страницами
 * @reactProps {number} dictionary - дополнительные символы-ключи для маски
 * @reactProps {boolean} guide - @see https://github.com/text-mask/text-mask/blob/master/componentDocumentation.md#guide
 * @reactProps {boolean} keepCharPositions - @see https://github.com/text-mask/text-mask/blob/master/componentDocumentation.md#keepcharpositions
 * @reactProps {boolean} resetOnNotValid - сбрасывать / оставлять невалижное значение при потере фокуса
 * @reactProps {object} presetConfig - настройки пресета для InputMoney
 * @example
 * <InputMask onChange={this.onChange}
 *             mask="99 x 99"
 *             dictionary={{x: \[01]\}}
 *             placeholderChar='?'
 *             />
 */
class InputMask extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            value: props.value,
            guide: false,
        }
        this.valid = false
        this.dict = {
            9: /\d/,
            S: /[A-Za-z]/,
            Б: /[А-я]/,
            ...props.dictionary,
        }
    }

  /**
   * преобразует маску-функцию, маску-строку в массив-маску (с regexp вместо символов) при помощи _mapToArray
   * @returns (number) возвращает массив-маску
   */
  mask = () => {
      const { mask } = this.props
      if (Array.isArray(mask)) {
          return mask
      } if (typeof mask === 'function') {
          return mask()
      }
      return this._mapToArray(mask)
  };

  /**
   * возвращает маку для пресета
   * @returns (number) возвращает массив-маску для пресета-аргумента
   */
  preset = (preset) => {
      const { presetConfig } = this.props
      switch (preset) {
          case 'phone':
              return this._mapToArray('+9 (999)-999-99-99')
          case 'post-code':
              return this._mapToArray('999999')
          case 'date':
              return this._mapToArray('99.99.9999')
          case 'money':
              return createNumberMask(presetConfig)
          case 'percentage':
              return createNumberMask({ prefix: '', suffix: '%' })
          case 'card':
              return this._mapToArray('9999 9999 9999 9999')
      }
  };

  /**
   * возвращает индекс первого символа маски, который еще не заполнен
   * @returns (number) индекс первого символа маски, который еще не заполнен
   */
  _indexOfFirstPlaceHolder = (value = '') => value.toString().indexOf(this.props.placeholderChar);

  /**
   * возвращает индекс последнего символа маски, который еще не заполнен
   * @returns (number) индекс последнего символа маски, который еще не заполнен
   */
  _indexOfLastPlaceholder = (mask) => {
      if (typeof mask === 'function') {
          return mask()
              .map(item => item instanceof RegExp)
              .lastIndexOf(true)
      } if (typeof mask === 'string') {
          return Math.max(
              ...Object.keys(this.dict).map(char => mask.lastIndexOf(char)),
          )
      } if (Array.isArray(mask)) {
          return mask.map(item => item instanceof RegExp).lastIndexOf(true)
      }
      return -1
  };

  /**
   * проверка на валидность (соответсвие маске)
   */
  _isValid = (value) => {
      const { preset, mask, guide } = this.props

      if (guide) {
          return value && this._indexOfFirstPlaceHolder(value) === -1
      }

      return (
          value.length > this._indexOfLastPlaceholder(this.preset(preset) || mask)
      )
  };

  /**
   * преобразование строки маски в массив ( уже  с регулярными выражениями)
   */
  _mapToArray = mask => mask.split('').map(char => (this.dict[char] ? this.dict[char] : char));

  _onChange = (e) => {
      const { value } = e.target

      this.valid = this._isValid(value)

      this.setState({ value, guide: this.props.guide }, () => {
          (this.valid || value === '') && this.props.onChange(value)
      })
  };

  _onBlur = (e) => {
      const { resetOnNotValid, onBlur } = this.props
      const { value } = e.nativeEvent.target
      this.valid = this._isValid(value)
      onBlur(value)
      if (!this.valid) {
          const newValue = resetOnNotValid ? '' : value
          this.setState(
              { value: newValue, guide: false },
              () => value === '' && this.props.onChange(newValue),
          )
      }
  };

  _onFocus = () => {
      this.valid = this._isValid(this.state.value)
      if (!this.valid) {
          this.setState({ guide: this.props.guide })
      }
  };

  /**
   * обработка новых пропсов
   */
  componentDidUpdate(prevProps) {
      const { value: valueFromState } = this.state
      const { value: valueFromProps } = this.props

      if (
          !isEqual(prevProps.value, valueFromProps) &&
      !isEqual(valueFromProps, valueFromState)
      ) {
          this.setState({
              value: this._isValid(valueFromProps) ? valueFromProps : '',
          })
      }

      this.dict = { ...this.dict, ...this.props.dictionary }
      this.valid = this._isValid(this.state.value)
  }

  /**
   * базовый рендер компонента
   */
  render() {
      const {
          preset,
          placeholderChar,
          placeholder,
          className,
          autoFocus,
          disabled,
      } = this.props
      const mask = this.preset(preset)

      return (
          <MaskedInput
              disabled={disabled}
              className={cn([
                  'form-control',
                  'n2o-input-mask',
                  { [className]: className },
              ])}
              placeholderChar={placeholderChar}
              placeholder={placeholder}
              guide={this.state.guide}
              mask={mask || this.mask}
              value={this.state.value}
              onBlur={this._onBlur}
              onChange={this._onChange}
              onFocus={this._onFocus}
              keepCharPositions={this.props.keepCharPositions}
              render={(ref, props) => (
                  <input
                      ref={ref}
                      {...omit(props, ['defaultValue'])}
                      autoFocus={autoFocus}
                  />
              )}
          />
      )
  }
}

InputMask.defaultProps = {
    onChange: () => {},
    placeholderChar: '_',
    guide: true,
    keepCharPositions: false,
    resetOnNotValid: true,
    value: '',
    dictionary: {},
    mask: '',
    presetConfig: {},
    onBlur: () => {},
    disabled: false,
}

InputMask.propTypes = {
    /**
   * Класс контрола
   */
    className: PropTypes.string,
    /**
   * Пресет маски
   */
    preset: PropTypes.string,
    /**
   * Маска
   */
    mask: PropTypes.oneOfType([
        PropTypes.string,
        PropTypes.array,
        PropTypes.func,
    ]),
    /**
   * Callback на изменение
   */
    onChange: PropTypes.func,
    /**
   * Placeholder контрола
   */
    placeholder: PropTypes.string,
    /**
   * Символ, который будет на месте незаполненного символа маски
   */
    placeholderChar: PropTypes.string,
    /**
   * Значение
   */
    value: PropTypes.string,
    /**
   * Дополнительные символы-ключи для маски
   */
    dictionary: PropTypes.object,
    /**
   * @see https://github.com/text-mask/text-mask/blob/master/componentDocumentation.md#guide
   */
    guide: PropTypes.bool,
    /**
   * @see https://github.com/text-mask/text-mask/blob/master/componentDocumentation.md#keepcharpositions
   */
    keepCharPositions: PropTypes.bool,
    /**
   * Сбрасывать / оставлять невалидное значение при потере фокуса
   */
    resetOnNotValid: PropTypes.bool,
    /**
   * Настройка пресета
   */
    presetConfig: PropTypes.object,
    /**
   * Callback на потерю фокуса
   */
    onBlur: PropTypes.func,
    disabled: PropTypes.bool,
}

export default compose(withRightPlaceholder)(InputMask)
