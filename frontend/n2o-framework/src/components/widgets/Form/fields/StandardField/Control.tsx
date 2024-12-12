import React, { ComponentType } from 'react'
import classNames from 'classnames'

import { Factory } from '../../../../../core/factory/Factory'
import { CONTROLS } from '../../../../../core/factory/factoryLevels'

export type RangeValue = { begin: string | number | null, end: string | number | null }

export interface ControlCommonProps {
    component: string | ComponentType<{ className?: string }>
    className?: string
}

export interface Props extends ControlCommonProps {
    value?: string | number | null
    model: Record<string, unknown>
    onChange?(event: React.ChangeEvent<HTMLInputElement> | RangeValue): void
    onFocus?(event: React.FocusEvent<HTMLInputElement> | RangeValue): void
    onBlur?(event: React.FocusEvent<HTMLInputElement> | RangeValue | number | string): void
    help?: string
    enabled?: boolean
    disabled?: boolean
    visible?: boolean
    autoFocus?: boolean
    placeholder?: string
}

/**
 * Контрол поля формы
 * @example
 * <Control component={Input} {...props}/>
 */
export const Control = ({ component, className, ...props }: Props) => {
    if (typeof component !== 'string') {
        return React.createElement(component, { ...props, className })
    }

    return <Factory level={CONTROLS} src={component} {...props} className={classNames('form-control', className)} />
}

export default Control
