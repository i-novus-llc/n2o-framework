import React from 'react'
import isNil from 'lodash/isNil'
import RCSwitch from 'rc-switch'
import type { SwitchChangeEventHandler } from 'rc-switch'

import { TBaseInputProps, TBaseProps } from '../types'
import '../styles/controls/Switch.scss'

export type Props = TBaseProps & TBaseInputProps<string | number | boolean> & {
    checked?: boolean
    label?: string
    onChange?: SwitchChangeEventHandler
}

export const Switch = ({ disabled, value, checked, onChange, label, id }: Props) => {
    const isChecked = isNil(checked) ? !!value : checked

    return (
        <div className="n2o-switch-wrapper">
            <RCSwitch
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
