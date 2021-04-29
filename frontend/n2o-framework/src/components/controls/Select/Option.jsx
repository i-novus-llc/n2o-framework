import React from 'react'
import PropTypes from 'prop-types'

/**
 * Компонент - опция селекта.
 * @reactProps {number|string} value - значение опции
 * @reactProps {boolean} disabled - задизейблена / нет
 * @reactProps {number|string} label - лейбл
 */

export function Option({ value, label, disabled }) {
    return (
        <option value={value} disabled={disabled}>
            {label}
        </option>
    )
}

Option.propTypes = {
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    label: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    disabled: PropTypes.bool,
}

export default Option
