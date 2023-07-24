import React from 'react'
import isNil from 'lodash/isNil'
import Switch from 'rc-switch'
import type { SwitchChangeEventHandler } from 'rc-switch'

import { TBaseInputProps, TBaseProps } from '../../types'
import '../../styles/controls/Switch.scss'

type N2OSwitchProps = TBaseProps & TBaseInputProps<string | number | boolean> & {
    checked?: boolean
    label?: string
    onChange?: SwitchChangeEventHandler
}

export const N2OSwitch = ({ disabled, value, checked, onChange, label, id }: N2OSwitchProps) => {
    const isChecked = isNil(checked) ? !!value : checked

    return (
        <div className="n2o-switch-wrapper">
            <Switch
                id={id}
                prefixCls="n2o-switch"
                checked={isChecked}
                disabled={disabled}
                onChange={onChange}
            />
            {label && <label htmlFor={id} className="n2o-switch-label">{label}</label>}
        </div>
    )
}
