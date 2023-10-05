import React, { MouseEventHandler } from 'react'
import classNames from 'classnames'

import { TBaseProps } from '../types'

type Props = TBaseProps & {
    bordered?: boolean
    circular?: boolean
    name?: string
    onClick?: MouseEventHandler<HTMLElement>
    spin?: boolean
    visible?: boolean
}

export function Icon({
    name = '',
    className,
    disabled = false,
    spin = false,
    circular = false,
    bordered = false,
    style,
    onClick = () => {},
    visible = true,
}: Props) {
    if (!visible) {
        return null
    }

    const iconClass = classNames({
        'n2o-icon': true,
        [name]: name,
        [className || '']: className,
        disabled,
        'fa-spin': spin,
        circular,
        bordered,
    })

    return <i className={iconClass} style={style} onClick={onClick} />
}
