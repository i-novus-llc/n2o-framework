import React, { useState } from 'react'
import PropTypes from 'prop-types'
import uniqueId from 'lodash/uniqueId'
import classNames from 'classnames'

import Input from '../Input/Input'

/**
 * @typedef {'default' | 'btn' | 'tabs' } RadioType
 */

/**
 * @type {Record<string, RadioType>}
 * @const RadioTypes
 */
export const RadioTypes = {
    button: 'btn',
    tabs: 'tabs',
    input: 'default',
}

export function InputRadio({ label, checked, disabled, onChange, type, name, value, className }) {
    const [elementId] = useState(uniqueId('n2o-radio-'))

    return (
        <label
            className={classNames('n2o-radio-input', `n2o-radio-input-${type}`, className, {
                checked,
                active: checked,
                disabled,
            })}
            htmlFor={elementId}
        >
            <Input
                className="alt-radio"
                disabled={disabled}
                type="radio"
                checked={checked}
                onChange={onChange}
                id={elementId}
                name={name}
                value={value}
            />
            <span>{label}</span>
        </label>
    )
}

InputRadio.propTypes = {
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    checked: PropTypes.bool,
    onChange: PropTypes.func,
    disabled: PropTypes.bool,
    label: PropTypes.node,
    name: PropTypes.string,
    className: PropTypes.string,
    type: PropTypes.oneOf([RadioTypes.input, RadioTypes.button, RadioTypes.tabs]),
}

InputRadio.defaultProps = {
    checked: false,
    disabled: false,
    type: RadioTypes.input,
}
