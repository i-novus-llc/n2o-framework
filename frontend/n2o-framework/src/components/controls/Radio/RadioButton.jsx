import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import { setDisplayName } from 'recompose'

import Input from '../Input/Input'

/**
 * Компонент RadioButton - радио в виде кнопок
 * @reactProps {string|number} value - уникально определяет элемент
 * @reactProps {boolean} checked - начальное значение
 * @reactProps {boolean} disabled - только для чтения / нет
 * @reactProps {function} onChange - вызывается при изменении значения
 * @reactProps {string} label - лейбл
 */

function RadioButton({ label, disabled, value, checked, onChange }) {
    return (
        // eslint-disable-next-line jsx-a11y/label-has-associated-control
        <label
            className={classNames('btn btn-secondary', {
                active: checked,
                disabled,
            })}
            /* eslint-disable-next-line jsx-a11y/no-noninteractive-tabindex,jsx-a11y/tabindex-no-positive */
            tabIndex={1}
        >
            <Input
                className="alt-radio"
                disabled={disabled}
                type="radio"
                value={value}
                checked={checked}
                onChange={onChange}
            />
            {label}
        </label>
    )
}

RadioButton.propTypes = {
    /**
     * Значение
     */
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    /**
     * Checked контрола
     */
    checked: PropTypes.bool,
    /**
     * Callback на изменение
     */
    onChange: PropTypes.func,
    /**
     * Флаг активности
     */
    disabled: PropTypes.bool,
    /**
     * Label контрола
     */
    label: PropTypes.string,
}

RadioButton.defaultProps = {
    checked: false,
    disabled: false,
}

export default setDisplayName('RadioButton')(RadioButton)
