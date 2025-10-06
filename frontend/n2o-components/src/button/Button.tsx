import React, { CSSProperties, ReactNode } from 'react'
import classNames from 'classnames'
import { Button as Component, ButtonProps as ComponentProps } from 'reactstrap'

export interface ButtonProps extends ComponentProps {
    rounded?: boolean
    className?: string
    style?: CSSProperties
    disabled?: boolean
    visible?: boolean
    children?: ReactNode
}

export function Button({
    children,
    className,
    rounded,
    disabled = false,
    visible = true,
    ...rest
}: ButtonProps) {
    if (!visible) { return null }

    return (
        <Component
            className={classNames(className, {
                'btn-rounded': rounded,
                'btn-disabled': disabled,
            })
            }
            disabled={disabled}
            {...rest}
        >
            {children}
        </Component>
    )
}
