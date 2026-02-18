import React, { ComponentProps, isValidElement, ReactNode } from 'react'
import classNames from 'classnames'

type Props = ComponentProps<'i'> & {
    name?: ReactNode | string
    disabled?: boolean
}

export function Icon({
    name,
    className,
    disabled = false,
    ...props
}: Props) {
    if (!name) { return null }

    if (isValidElement(name)) { return name }

    const iconClass = classNames('n2o-icon', name, className, {
        disabled,
    })

    return <i className={iconClass} {...props} />
}
