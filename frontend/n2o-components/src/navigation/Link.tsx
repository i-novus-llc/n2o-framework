import React from 'react'
import classNames from 'classnames'

import { Icon } from '../display/Icon'

import { type LinkProps, Position } from './types'

export function Link({
    label,
    url,
    icon,
    target,
    className,
    style,
    onClick,
    iconPosition = Position.LEFT,
    disabled = false,
}: LinkProps) {
    const content = (
        <>
            {iconPosition === Position.LEFT && <Icon name={icon} />}
            {label && <span>{label}</span>}
            {iconPosition === Position.RIGHT && <Icon name={icon} />}
        </>
    )

    const classes = classNames(className, { disabled })

    const props = {
        className: classes,
        style,
        rel: target === '_blank' ? 'noopener noreferrer' : undefined,
        target,
    }

    if (disabled) { return <span {...props} aria-disabled>{content}</span> }

    return <a {...props} href={url} onClick={onClick}>{content}</a>
}

Link.displayName = '@n2o-components/navigation/Link'
