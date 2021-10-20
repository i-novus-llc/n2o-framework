import React, { useCallback } from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import toString from 'lodash/toString'

import { InputRadio, RadioTypes } from './Input'

export function Group({
    value,
    visible,
    style,
    className,
    inline,
    disabled,
    options,
    type,
    name,
    onChange,
}) {
    const changeHandler = useCallback(event => onChange(event.target.value), [onChange])

    if (visible === false) { return <></> }

    const children = options.map(radio => (
        <InputRadio
            {...radio}
            key={radio.value}
            name={name}
            type={type}
            disabled={disabled || radio.disabled}
            checked={toString(radio.value) === toString(value)}
            onChange={changeHandler}
        />
    ))

    return (
        <div
            className={classNames(
                className,
                'n2o-radio-group',
                `n2o-radio-group-${type}`,
                inline ? 'n2o-radio-group-inline' : 'n2o-radio-group-vertical',
            )}
            style={style}
        >
            {children}
        </div>
    )
}

Group.propTypes = {
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    onChange: PropTypes.func,
    disabled: PropTypes.bool,
    visible: PropTypes.bool,
    style: PropTypes.object,
    className: PropTypes.string,
    name: PropTypes.string,
    inline: PropTypes.bool,
    options: PropTypes.arrayOf(PropTypes.shape({
        value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
        disabled: PropTypes.bool,
        label: PropTypes.node,
        className: PropTypes.string,
    })),
    type: PropTypes.oneOf([RadioTypes.input, RadioTypes.button, RadioTypes.tabs]),
}

Group.defaultProps = {
    visible: true,
    inline: false,
    type: RadioTypes.input,
    onChange: () => {},
}

export { RadioTypes }
