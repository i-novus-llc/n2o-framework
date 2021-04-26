import React from 'react'
import PropTypes from 'prop-types'

const measureStyle = { marginLeft: 5 }

/**
 * Компонент-размерность
 * @param {string} value - размерность. Например: км, л, штук...
 * @param {object} props - остальные пропсы
 * @example
 * <Measure value="м"/>
 */
const Measure = ({ value, ...props }) => (value ? (
    <span style={measureStyle} {...props}>
        {value}
    </span>
) : null)

Measure.propTypes = {
    value: PropTypes.string,
}

export default Measure
