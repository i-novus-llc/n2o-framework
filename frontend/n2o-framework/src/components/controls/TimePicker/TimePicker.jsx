import React, { Component } from 'react'
import { findDOMNode } from 'react-dom'
import PropTypes from 'prop-types'
import moment from 'moment'
import includes from 'lodash/includes'
import pick from 'lodash/pick'
import keys from 'lodash/keys'
import join from 'lodash/join'
import map from 'lodash/map'
import get from 'lodash/get'
import each from 'lodash/each'
import split from 'lodash/split'
import cn from 'classnames'
import { Row, Col } from 'reactstrap'
import { Manager, Reference, Popper } from 'react-popper'
import scrollIntoView from 'scroll-into-view-if-needed'

import InputText from '../InputText/InputText'
import { InputIcon } from '../../snippets/InputIcon/InputIcon'
import { MODIFIERS } from '../DatePicker/utils'

const HOURS = 'hours'
const MINUTES = 'minutes'
const SECONDS = 'seconds'

const DIGIT = 'digit'
const SYMBOLS = 'symbols'

const reference = {
    [HOURS]: {
        format: 'HH',
        en: 'h_hours',
        ru: 'ч_часы',
        values: 24,
    },
    [MINUTES]: {
        format: 'mm',
        en: 'min_minutes',
        ru: 'мин_минуты',
        values: 60,
    },
    [SECONDS]: {
        format: 'ss',
        en: 'sec_seconds',
        ru: 'сек_секунды',
        values: 60,
    },
}

/**
 * Компонент TimePicker
 */
export class TimePickerControl extends Component {
    constructor(props) {
        super(props)
        this.value = moment(props.value || props.defaultValue, props.timeFormat)
        this.state = {
            open: false,
            [HOURS]: this.value.hours(),
            [MINUTES]: this.value.minutes(),
            [SECONDS]: this.value.seconds(),
        }
        this[`${HOURS}Ref`] = React.createRef()
        this[`${MINUTES}Ref`] = React.createRef()
        this[`${SECONDS}Ref`] = React.createRef()
        this.controlRef = React.createRef()
    }

    componentDidMount() {
        if (typeof window !== 'undefined') {
            document.addEventListener('mousedown', this.onClickOutside)
            document.addEventListener('touchstart', this.onClickOutside)
        }
    }

    componentDidUpdate(prevProps, prevState) {
        const { open } = this.state
        const { value, timeFormat } = this.props

        const hasChangeVisible = open !== prevState.open

        if (prevProps.value !== value) {
            this.value = moment(value, timeFormat)
            this.setState({
                [HOURS]: this.value.hours(),
                [MINUTES]: this.value.minutes(),
                [SECONDS]: this.value.seconds(),
            })
        }

        each([HOURS, MINUTES, SECONDS], (mode) => {
            if (this[`${mode}Ref`] && this[`${mode}Ref`].current) {
                scrollIntoView(this[`${mode}Ref`].current, {
                    behavior: hasChangeVisible ? 'auto' : 'smooth',
                    block: 'start',
                    boundary: this[`${mode}Ref`].current.parentElement,
                })
            }
        })
    }

    componentWillUnmount() {
        if (typeof window !== 'undefined') {
            document.removeEventListener('mousedown', this.onClickOutside)
            document.removeEventListener('touchstart', this.onClickOutside)
        }
    }

  onClickOutside = (e) => {
      const { open } = this.state

      if (open) {
          // eslint-disable-next-line react/no-find-dom-node
          const controlEl = findDOMNode(this.controlRef.current)

          if (
              e.target.className.includes('n2o-pop-up') ||
        !controlEl.contains(e.target)
          ) {
              this.handleClose()
          }
      }
  };

    getTimeConfig = () => {
        const { mode } = this.props

        return {
            showHour: includes(mode, 'hours'),
            showMinute: includes(mode, 'minutes'),
            showSecond: includes(mode, 'seconds'),
        }
    };

    // eslint-disable-next-line react/destructuring-assignment
    toTime = value => (value < 10 && !this.props.noZero ? `0${value}` : value)

    getLocaleText = (mode, index) => {
        const { locale } = this.props
        const localesArr = split(get(reference, `[${mode}][${locale}]`, ''), '_')

        return localesArr[index]
    }

    getTime = format => (this.value.isValid() ? this.value.format(format) : '')

    // eslint-disable-next-line consistent-return
    getValue = () => {
        const { format, timeFormat, mode } = this.props

        if (format === DIGIT) {
            return this.getTime(timeFormat)
        }
        if (format === SYMBOLS) {
            return this.getTime(
                join(
                    map(
                        keys(pick(reference, mode)),
                        mode => `${reference[mode].format} [${this.getLocaleText(mode, 0)}]`,
                    ),
                    ' ',
                ),
            )
        }
    };

  handleOpen = () => {
      const { disabled } = this.props

      if (disabled) { return }
      this.setState({ open: true })
  };

  handleClose = () => {
      const { disabled } = this.props

      if (disabled) { return }
      this.setState({ open: false })
  };

  handleToggle = () => {
      const { disabled } = this.props

      if (disabled) { return }
      this.setState(state => ({ ...state, open: !state.open }))
  };

  handleChangeValue = (mode, value) => (e) => {
      const {
          [HOURS]: hours,
          [MINUTES]: minutes,
          [SECONDS]: seconds,
      } = this.state

      e.preventDefault()

      const prevState = {
          [HOURS]: hours || 0,
          [MINUTES]: minutes || 0,
          [SECONDS]: seconds || 0,
      }

      if (!this.value.isValid()) {
          this.value = moment()
          this.value.set(prevState)
      }
      this.value.set(mode, value)

      this.setState(
          {
              ...prevState,
              [mode]: value,
          },
          () => {
              const { timeFormat, onChange } = this.props

              onChange(this.getTime(timeFormat))
          },
      )
  };

    handlePrevent = (e) => {
        e.preventDefault()
    };

    renderPrefix = () => {
        const { prefix } = this.props

        if (prefix) {
            return <span className="time-prefix">{prefix}</span>
        }

        return null
    }

  renderPanelItems = (mode) => {
      const countersArray = new Array(get(reference, `[${mode}].values`, []))
      const { [mode]: modeValue } = this.state

      return map(countersArray, (val, index) => (
          // eslint-disable-next-line jsx-a11y/anchor-is-valid
          <a
              key={index}
              ref={index === modeValue ? this[`${mode}Ref`] : null}
              href="#"
              className={cn('dropdown-item n2o-time-picker__panel__item', {
                  active: index === modeValue,
              })}
              onMouseDown={this.handleChangeValue(mode, index)}
              onClick={this.handlePrevent}
          >
              {this.toTime(index)}
          </a>
      ))
  };

  render() {
      const { placeholder, disabled } = this.props
      const { open } = this.state
      const timeConfig = this.getTimeConfig()
      const readyValue = this.getValue()
      const prefixCmp = this.renderPrefix()

      return (
          <div className="n2o-time-picker" ref={this.controlRef}>
              <Manager>
                  <Reference>
                      {({ ref }) => (
                          <InputText
                              inputRef={ref}
                              className="n2o-time-picker__input"
                              prefix={prefixCmp}
                              suffix={(
                                  <InputIcon clickable hoverable onClick={this.handleToggle}>
                                      <span
                                          className="fa fa-chevron-down"
                                          style={{
                                              transition: 'transform 150ms linear',
                                              transform: open ? 'rotate(-180deg)' : 'rotate(0deg)',
                                          }}
                                      />
                                  </InputIcon>
                              )}
                              readOnly
                              placeholder={placeholder}
                              disabled={disabled}
                              value={readyValue}
                              onClick={this.handleToggle}
                          />
                      )}
                  </Reference>
                  {open ? (
                      <Popper
                          placement="bottom-end"
                          strategy="fixed"
                          modifiers={MODIFIERS}
                      >
                          {({ ref, style, placement }) => (
                              <div
                                  ref={ref}
                                  style={style}
                                  data-placement={placement}
                                  className="n2o-pop-up"
                              >
                                  <Row noGutters>
                                      {timeConfig.showHour ? (
                                          <Col>
                                              <div className="n2o-time-picker__header">
                                                  {this.getLocaleText(HOURS, 1)}
                                              </div>
                                              <div className="n2o-time-picker__panel">
                                                  {this.renderPanelItems(HOURS)}
                                              </div>
                                          </Col>
                                      ) : null}
                                      {timeConfig.showMinute ? (
                                          <Col>
                                              <div className="n2o-time-picker__header">
                                                  {this.getLocaleText(MINUTES, 1)}
                                              </div>
                                              <div className="n2o-time-picker__panel">
                                                  {this.renderPanelItems(MINUTES)}
                                              </div>
                                          </Col>
                                      ) : null}
                                      {timeConfig.showSecond ? (
                                          <Col>
                                              <div className="n2o-time-picker__header last">
                                                  {this.getLocaleText(SECONDS, 1)}
                                              </div>
                                              <div className="n2o-time-picker__panel">
                                                  {this.renderPanelItems(SECONDS)}
                                              </div>
                                          </Col>
                                      ) : null}
                                  </Row>
                              </div>
                          )}
                      </Popper>
                  ) : null}
              </Manager>
          </div>
      )
  }
}

TimePickerControl.propTypes = {
    /**
   * Значение компонента
   */
    value: PropTypes.string,
    /**
   * Префикс
   */
    prefix: PropTypes.string,
    /**
   * Режим отображения попапа времени ["hours", "minutes", "seconds"]
   */
    mode: PropTypes.array,
    /**
   * Формат времени для модели
   */
    timeFormat: PropTypes.string,
    /**
   * Формат врмени для отображения "digit" || "symbols"
   */
    format: PropTypes.oneOf([DIGIT, SYMBOLS]),
    /**
   * Дефолтное значение
   */
    defaultValue: PropTypes.string,
    /**
   * onChange
   */
    onChange: PropTypes.func,
    /**
   * Локализация
   */
    locale: PropTypes.oneOf(['ru', 'en']),
    /**
   * Плэйсхолдер
   */
    placeholder: PropTypes.string,
    /**
   * On/Off
   */
    disabled: PropTypes.bool,
    /**
   * Нет нулей перед во времени. Пример - 5 минут, а не 05 минут.
   */
    noZero: PropTypes.bool,
}

TimePickerControl.defaultProps = {
    mode: ['hours', 'minutes', 'seconds'],
    format: 'symbols',
    timeFormat: 'HH:mm:ss',
    locale: 'ru',
    onChange: () => {},
}

export default TimePickerControl
