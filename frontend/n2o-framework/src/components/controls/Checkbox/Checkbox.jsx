import React from 'react'
import PropTypes from 'prop-types'
import isNil from 'lodash/isNil'
import { setDisplayName } from 'recompose'
import cn from 'classnames'

import Input from '../Input/Input'

/**
 * Компонент Checkbox
 * @reactProps {string|number} value - уникально определяет элемент
 * @reactProps {boolean} checked - начальное значение
 * @reactProps {boolean} disabled -только для чтения / нет
 * @reactProps {function} onChange - вызывается при изменении значения
 * @reactProps {string} label - лейбл
 */
function Checkbox({
    label,
    disabled,
    value,
    checked,
    onChange,
    onFocus,
    onBlur,
    className,
    autoFocus,
}) {
    return (
        <div className={cn('checkbox', className)}>
            {/* eslint-disable-next-line jsx-a11y/label-has-associated-control */}
            <label>
                <Input
                    type="checkbox"
                    disabled={disabled}
                    value={value}
                    checked={isNil(checked) ? !!value : checked}
                    onChange={onChange}
                    onFocus={onFocus}
                    onBlur={onBlur}
                    autoFocus={autoFocus}
                />
                {' '}
                {label}
            </label>
        </div>
    )
}

Checkbox.propTypes = {
    /**
   * Значение контрола
   */
    value: PropTypes.any,
    /**
   * Callback на изменение
   */
    onChange: PropTypes.func,
    /**
   * Флаг активности контрола
   */
    disabled: PropTypes.bool,
    /**
   * Label контрола
   */
    label: PropTypes.string,
    /**
   * Checked контрола
   */
    checked: PropTypes.bool,
    className: PropTypes.string,
    /**
   * Авто фокус на контроле
   */
    autoFocus: PropTypes.bool,
    /**
   * Callback на фокус
   */
    onFocus: PropTypes.func,
    /**
   * Callback на потерю фокуса
   */
    onBlur: PropTypes.func,
}

Checkbox.defaultProps = {
    disabled: false,
    onFocus: () => {},
    onBlur: () => {},
    onChange: () => {},
}

export default setDisplayName('Checkbox')(Checkbox)
