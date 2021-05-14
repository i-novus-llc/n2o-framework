import React from 'react'
import PropTypes from 'prop-types'
import cx from 'classnames'
import isNil from 'lodash/isNil'
import { setDisplayName } from 'recompose'

import Input from '../Input/Input'

/**
 * Компонент чекбоксов в виде кнопок
 * @reactProps {string|number} value - уникально определяет элемент
 * @reactProps {boolean} checked - начальное значение
 * @reactProps {boolean} disabled - только для чтения / нет
 * @reactProps {function} onChange - вызывается при изменении значения
 * @reactProps {string} label - лейбл
 */
function CheckboxButton({
    label,
    disabled,
    value,
    checked,
    onChange,
    onFocus,
    onBlur,
    className,
}) {
    return (
    // eslint-disable-next-line jsx-a11y/label-has-associated-control
        <label
            className={cx('btn btn-secondary', className, {
                active: checked,
                disabled,
            })}
        >
            <Input
                disabled={disabled}
                type="checkbox"
                value={value}
                checked={isNil(checked) ? !!value : checked}
                onChange={onChange}
                onFocus={onFocus}
                onBlur={onBlur}
            />
            {label}
        </label>
    )
}

CheckboxButton.propTypes = {
    /**
     * Значение
     */
    value: PropTypes.any,
    /**
     * Checked
     */
    checked: PropTypes.bool,
    /**
     * Callback изменения
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
    /**
     * Класс
     */
    className: PropTypes.string,
    /**
     * Callback фокуса
     */
    onFocus: PropTypes.func,
    /**
     * Callback потери фокуса
     */
    onBlur: PropTypes.func,
}

CheckboxButton.defaultProps = {
    checked: false,
    disabled: false,
    onFocus: () => {},
    onBlur: () => {},
    onChange: () => {},
}

export default setDisplayName('CheckboxButton')(CheckboxButton)
