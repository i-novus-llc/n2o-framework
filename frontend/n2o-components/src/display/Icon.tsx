import React, { ComponentProps } from 'react'
import classNames from 'classnames'

type Props = ComponentProps<'i'> & {
    name?: string
    disabled?: boolean
}

export function Icon({
    name = '',
    className,
    disabled = false,
    ...props
}: Props) {
    if (!name) { return null }

    const iconClass = classNames('n2o-icon', {
        [name]: name,
        [className || '']: className,
        disabled,
    })

    return <i className={iconClass} {...props} />
}
