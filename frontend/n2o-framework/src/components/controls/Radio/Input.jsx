import React, { useState } from 'react'
import PropTypes from 'prop-types'
import uniqueId from 'lodash/uniqueId'
import classNames from 'classnames'
import { UncontrolledTooltip } from 'reactstrap'

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

export function InputRadio({
    label,
    checked,
    disabled,
    onChange,
    type,
    name,
    value,
    className,
    invalid,
    tooltip,
}) {
    const [elementId] = useState(uniqueId('n2o-radio-'))
    const [target] = useState(uniqueId('n2o-radio-'))

    return (
        <label
            id={target}
            className={classNames('n2o-radio-input', `n2o-radio-input-${type}`, className, {
                checked,
                active: checked,
                disabled,
                invalid,
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
            {tooltip && <UncontrolledTooltip target={target}>{tooltip}</UncontrolledTooltip>}
        </label>
    )
}

InputRadio.propTypes = {
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    checked: PropTypes.bool,
    invalid: PropTypes.bool,
    onChange: PropTypes.func,
    disabled: PropTypes.bool,
    tooltip: PropTypes.string,
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
