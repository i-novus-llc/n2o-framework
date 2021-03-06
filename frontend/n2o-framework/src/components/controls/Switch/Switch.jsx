import React from 'react'
import PropTypes from 'prop-types'
import isNil from 'lodash/isNil'
import Switch from 'rc-switch'

function N2OSwitch({ disabled, value, checked, onChange, label, id }) {
    return (
        <div className="n2o-switch-wrapper">
            <Switch
                id={id}
                prefixCls="n2o-switch"
                checked={isNil(checked) ? !!value : checked}
                disabled={disabled}
                onChange={onChange}
            />
            {/* eslint-disable-next-line jsx-a11y/label-has-associated-control */}
            {label && <label className="n2o-switch-label">{label}</label>}
        </div>
    )
}

N2OSwitch.propTypes = {
    /**
     * Значение
     */
    value: PropTypes.oneOfType([
        PropTypes.string,
        PropTypes.number,
        PropTypes.bool,
    ]),
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
     * Текст
     */
    label: PropTypes.string,
    /**
     * Идентификатор
     */
    id: PropTypes.string,
}

export default N2OSwitch
