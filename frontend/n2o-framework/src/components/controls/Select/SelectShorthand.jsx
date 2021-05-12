import React from 'react'
import PropTypes from 'prop-types'

import { Select } from './Select'
import { Option } from './Option'

/**
 * SelectShorthand
 * @reactProps {function} onChange
 * @reactProps {boolean} required - обязательность поля
 * @reactProps {boolean} autoFocus - с автофокусом селект или нет
 * @reactProps {boolean} disabled - задизейблен или нет селект
 * @reactProps {boolean} visible
 * @reactProps {string|number} heightSize - css-класс, определяющий высоту. Варианты: 'form-control-lg', 'form-control-sm', ''
 * @reactProps {string|number} defaultValue - дефолтное значение
 * @reactProps {array} options - варианты выбора
 */

export function SelectShorthand(props) {
    const { options } = props

    return (
        <Select {...props}>
            {options.map(option => (
                <Option
                    value={option.value}
                    label={option.label}
                    disabled={option.disabled}
                />
            ))}
        </Select>
    )
}

SelectShorthand.propTypes = {
    onChange: PropTypes.func,
    required: PropTypes.bool,
    autoFocus: PropTypes.bool,
    disabled: PropTypes.bool,
    visible: PropTypes.bool,
    heightSize: PropTypes.oneOf(['input-sm', 'input-lg', '']),
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    options: PropTypes.array,
}

SelectShorthand.defaultProps = {
    autoFocus: false,
    disabled: false,
    required: false,
    visible: true,
}

export default SelectShorthand
