import React from 'react'
import PropTypes from 'prop-types'
/**
 * Описание поля
 * @param {object} props - пропсы
 * @param {string} props.value - текст описания
 * @example
 * <Description value='Введите номер телефона'/>
 */

const descriptionStyle = { fontSize: '0.8em' }
const Description = ({ value, ...props }) => (
    <div {...props} style={descriptionStyle}>
        {value}
    </div>
)

Description.propTypes = {
    value: PropTypes.string,
}

export default Description
