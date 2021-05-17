import React from 'react'
import PropTypes from 'prop-types'
import { setDisplayName } from 'recompose'
import classNames from 'classnames'

import Input from '../Input/Input'

/**
 * Компонент Radio
 * @reactProps {string|number} value - уникально определяет элемент
 * @reactProps {boolean} checked - начальное значение
 * @reactProps {boolean} disabled -только для чтения / нет
 * @reactProps {function} onChange - вызывается при изменении значения
 * @reactProps {string} compileLabel - лейбл
 */
function Radio({ label, disabled, value, checked, onChange }) {
    return (
        <div className={classNames('radio', { checked })}>
            {/* eslint-disable-next-line jsx-a11y/label-has-associated-control */}
            <label>
                <Input
                    disabled={disabled}
                    type="radio"
                    value={value}
                    checked={checked}
                    onChange={onChange}
                />
                {' '}
                {label}
            </label>
        </div>
    )
}

Radio.propTypes = {
    /**
     * Значение
     */
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    /**
     * Checked контрола
     */
    checked: PropTypes.bool,
    /**
     * Флаг активности
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
}

export default setDisplayName('Radio')(Radio)
