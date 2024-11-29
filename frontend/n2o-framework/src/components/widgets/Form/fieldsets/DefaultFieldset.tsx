import React, { ReactNode } from 'react'
import classNames from 'classnames'

export interface Props {
    className: string
    disabled: boolean
    children: ReactNode
}

export function DefaultFieldset({ className, children, disabled = false, ...rest }: Props) {
    return (
        <div
            {...rest}
            className={classNames('default-fieldset', className, { 'n2o-disabled': disabled })}
        >
            {children}
        </div>
    )
}

export default DefaultFieldset
