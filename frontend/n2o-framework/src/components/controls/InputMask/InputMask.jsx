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
 * @reactProps {boolean} clearOnBlur - сбрасывать / оставлять невалижное значение при потере фокуса
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

      return this.mapToArray(mask)
  };

  /**
   * возвращает маку для пресета
   * @returns (number) возвращает массив-маску для пресета-аргумента
   */
  // eslint-disable-next-line consistent-return
  preset = (preset) => {
      const { presetConfig } = this.props

      switch (preset) {
          case 'phone':
              return this.mapToArray('+9 (999)-999-99-99')
          case 'post-code':
              return this.mapToArray('999999')
          case 'date':
              return this.mapToArray('99.99.9999')
          case 'money':
              return createNumberMask(presetConfig)
          case 'percentage':
              return createNumberMask({ prefix: '', suffix: '%' })
          case 'card':
              return this.mapToArray('9999 9999 9999 9999')
          default:
              return undefined
      }
  };

  /**
   * возвращает индекс первого символа маски, который еще не заполнен
   * @returns (number) индекс первого символа маски, который еще не заполнен
   */
  indexOfFirstPlaceHolder = (value = '') => {
      const { placeholderChar } = this.props

      return value.toString().indexOf(placeholderChar)
  }

  /**
   * возвращает индекс последнего символа маски, который еще не заполнен
   * @returns (number) индекс последнего символа маски, который еще не заполнен
   */
  indexOfLastPlaceholder = (mask) => {
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
  isValid = (value) => {
      const { preset, mask, guide } = this.props

      if (guide) {
          return value && this.indexOfFirstPlaceHolder(value) === -1
      }

      return (
          value.length > this.indexOfLastPlaceholder(this.preset(preset) || mask)
      )
  };

  /**
   * преобразование строки маски в массив ( уже  с регулярными выражениями)
   */
  mapToArray = mask => mask.split('').map(char => (this.dict[char] ? this.dict[char] : char));

  onChange = (e) => {
      const { value } = e.target
      const { guide, onChange } = this.props

      this.valid = this.isValid(value)

      this.setState({ value, guide }, () => {
          if (this.valid || value === '') {
              onChange(value)
          }
      })
  };

  onBlur = (e) => {
      const { onBlur, clearOnBlur, onChange } = this.props
      const { value } = e.nativeEvent.target

      this.valid = this.isValid(value)
      onBlur(value)
      if (!this.valid) {
          const newValue = clearOnBlur ? '' : value

          this.setState(
              { value: newValue, guide: false },
              () => value === '' && onChange(newValue),
          )
      }
  };

  onFocus = () => {
      const { guide } = this.props
      const { value } = this.state

      this.valid = this.isValid(value)
      if (!this.valid) {
          this.setState({ guide })
      }
  };

  /**
   * обработка новых пропсов
   */
  componentDidUpdate(prevProps) {
      const { value: valueFromState } = this.state
      const { value: valueFromProps, dictionary } = this.props

      if (
          !isEqual(prevProps.value, valueFromProps) &&
          !isEqual(valueFromProps, valueFromState)
      ) {
          this.setState({
              value: this.isValid(valueFromProps) ? valueFromProps : '',
          })
      }

      this.dict = { ...this.dict, ...dictionary }
      this.valid = this.isValid(valueFromState)
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
          keepCharPositions,
      } = this.props
      const { guide, value } = this.state
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
              guide={guide}
              mask={mask || this.mask}
              value={value}
              onBlur={this.onBlur}
              onChange={this.onChange}
              onFocus={this.onFocus}
              keepCharPositions={keepCharPositions}
              render={(ref, props) => (
                  <input
                      ref={ref}
                      {...omit(props, ['defaultValue'])}
                      /* eslint-disable-next-line jsx-a11y/no-autofocus */
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
    clearOnBlur: false,
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
    clearOnBlur: PropTypes.bool,
    /**
     * Настройка пресета
     */
    presetConfig: PropTypes.object,
    /**
     * Callback на потерю фокуса
     */
    onBlur: PropTypes.func,
    disabled: PropTypes.bool,
    autoFocus: PropTypes.bool,
}

export default compose(withRightPlaceholder)(InputMask)
