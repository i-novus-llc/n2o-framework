import React, { useState } from 'react'
import PropTypes from 'prop-types'
import uniqueId from 'lodash/uniqueId'
import classNames from 'classnames'
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

function RadioN2O({ label, disabled, value, checked, onChange, inline }) {
    const [elementId] = useState(uniqueId('checkbox-'))

    return (
        <div
            className={classNames('custom-control custom-radio', {
                'custom-control-inline': inline,
                checked,
            })}
        >
            <Input
                id={elementId}
                className="custom-control-input"
                disabled={disabled}
                type="radio"
                value={value}
                checked={checked}
                onChange={onChange}
            />
            <label className="custom-control-label" htmlFor={elementId}>
                {label}
            </label>
        </div>
    )
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
