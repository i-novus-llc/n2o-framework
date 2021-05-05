import React from 'react'
import PropTypes from 'prop-types'
import cn from 'classnames'
import { compose } from 'recompose'
import isFunction from 'lodash/isFunction'

import withRightPlaceholder from '../withRightPlaceholder'
import Input from '../Input/Input'

/**
 * Контрол Input
 * @reactProps {boolean} length - максимальная длина значения
 * @reactProps {string} value - значение поля
 * @reactProps {string} placeholder - плэйсхолдер
 * @reactProps {any} inputRef
 * @reactProps {boolean} disabled - флаг неактивности поля
 * @reactProps {boolean} disabled - флаг только для чтения
 * @reactProps {boolean} autoFocus - автофокус
 * @reactProps {function} onFocus - callback на фокус
 * @reactProps {function} onPaste - callback при вставке в инпут
 * @reactProps {function} onBlur - callback при блюре инпута
 * @reactProps {function} onChange - callback при изменение инпута
 * @reactProps {function} onKeyDown - callback при нажатии на кнопку клавиатуры
 * @reactProps {string} className - css-класс
 * @reactProps {object} style - объект стилей
 */
class InputText extends React.Component {
    constructor(props) {
        super(props)

        this.state = {
            focused: false,
        }

        this.inputRef = React.createRef()
    }

  onChange = (e) => {
      const { onChange } = this.props

      if (isFunction(onChange)) {
          onChange(e.target.value)
      }
  };

  onBlur = (e) => {
      const { onBlur } = this.props

      this.setState(() => ({
          focused: false,
      }))
      if (isFunction(onBlur)) {
          onBlur(e)
      }
  };

  onFocus = (e) => {
      const { onFocus } = this.props

      this.setState(() => ({
          focused: true,
      }))
      if (isFunction(onFocus)) {
          onFocus(e)
      }
  };

  handleClickAffix = () => {
      this.inputRef.current.focus()
  };

  render() {
      const {
          value,
          placeholder,
          length,
          disabled,
          inputRef,
          onPaste,
          onFocus,
          onBlur,
          onKeyDown,
          onClick,
          autoFocus,
          className,
          style,
          prefix,
          suffix,
          readOnly,
      } = this.props
      const { focused } = this.state

      const hasAffix = !!prefix || !!suffix

      const inputProps = {
          type: 'text',
          autoFocus,
          maxLength: length,
          value: value === null ? '' : value,
          placeholder,
          disabled,
          readOnly,
          onPaste,
          onKeyDown,
          onClick,
          onChange: this.onChange,
      }

      if (hasAffix) {
          return (
              <div
                  ref={inputRef}
                  className={cn(
                      'form-control n2o-input-text__affix-wrapper',
                      className,
                      {
                          focused,
                          disabled,
                      },
                  )}
                  style={style}
              >
                  {prefix ? (
                      <span
                          className={cn('n2o-input-text__prefix')}
                          onClick={this.handleClickAffix}
                      >
                          {prefix}
                      </span>
                  ) : null}
                  <Input
                      {...inputProps}
                      inputRef={this.inputRef}
                      className="n2o-input-text"
                      onFocus={this.onFocus}
                      onBlur={this.onBlur}
                  />
                  {suffix ? (
                      <span
                          className={cn('n2o-input-text__suffix')}
                          onClick={this.handleClickAffix}
                      >
                          {suffix}
                      </span>
                  ) : null}
              </div>
          )
      }

      return (
          <Input
              {...inputProps}
              className={cn('form-control n2o-input-text', className)}
              style={style}
              onFocus={onFocus}
              onBlur={onBlur}
              inputRef={inputRef}
          />
      )
  }
}

InputText.propTypes = {
    /**
   * Значение
   */
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    /**
   * Флаг активности
   */
    disabled: PropTypes.bool,
    /**
   * Callback на фокус
   */
    onFocus: PropTypes.func,
    /**
   * Callback на вставку значения
   */
    onPaste: PropTypes.func,
    /**
   * Callback на потерю фокуса
   */
    onBlur: PropTypes.func,
    /**
   * Callback на нажатие кнопки
   */
    onKeyDown: PropTypes.func,
    /**
   * Callback на нажатие кнопки
   */
    onClick: PropTypes.func,
    /**
   * Класс
   */
    className: PropTypes.string,
    /**
   * Стили
   */
    style: PropTypes.object,
    /**
   * Placeholder контрола
   */
    placeholder: PropTypes.string,
    /**
   * Callback на изменение
   */
    onChange: PropTypes.func,
    /**
   * Авто фокусировка на контроле
   */
    autoFocus: PropTypes.bool,
    /**
   * Максимальная длина
   */
    length: PropTypes.string,
    /**
   * Функция получения ref
   */
    inputRef: PropTypes.any,
    /**
   * prefix
   */
    prefix: PropTypes.node,
    /**
   * suffix
   */
    suffix: PropTypes.node,
    /**
   * readOnly
   */
    readOnly: PropTypes.bool,
}

InputText.defaultProps = {
    onChange: () => {},
    className: '',
    disabled: false,
    autoFocus: false,
}

export default compose(withRightPlaceholder)(InputText)
