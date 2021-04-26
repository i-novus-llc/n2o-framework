import React from 'react'
import PropTypes from 'prop-types'
import uniqueId from 'lodash/uniqueId'
import cx from 'classnames'
import { setDisplayName } from 'recompose'

import Input from '../Input/Input'

/**
 * Альтернативный радио контрол
 * @reactProps {string|number} value - уникально определяет элемент
 * @reactProps {boolean} checked - начальное значение
 * @reactProps {boolean} disabled -только для чтения / нет
 * @reactProps {function} onChange - вызывается при изменении значения
 * @reactProps {string} label - лейбл
 * @reactProps {boolean} inline - в ряд
 */

class RadioN2O extends React.Component {
    constructor(props) {
        super(props)

        this.elementId = uniqueId('checkbox-')
    }

    /**
   * Рендер
   */

    render() {
        const { label, disabled, value, checked, onChange, inline } = this.props

        return (
            <div
                className={cx('custom-control custom-radio', {
                    'custom-control-inline': inline,
                    checked,
                })}
            >
                <Input
                    id={this.elementId}
                    className="custom-control-input"
                    disabled={disabled}
                    type="radio"
                    value={value}
                    checked={checked}
                    onChange={onChange}
                />
                <label className="custom-control-label" htmlFor={this.elementId}>
                    {label}
                </label>
            </div>
        )
    }
}

RadioN2O.propTypes = {
    /**
   * Значение
   */
    value: PropTypes.any,
    /**
   * Checked контрола
   */
    checked: PropTypes.bool,
    /**
   * Флан активности
   */
    disabled: PropTypes.bool,
    /**
   * Callback на изменение
   */
    onChange: PropTypes.func,
    /**
   * Label контрола
   */
    label: PropTypes.string,
    /**
   * Флаг рендера label и контрола в одну линию
   */
    inline: PropTypes.bool,
}

RadioN2O.defaultProps = {
    disabled: false,
    checked: false,
    inline: false,
}

export default setDisplayName('RadioN2O')(RadioN2O)
