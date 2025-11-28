import React from 'react'
import classNames from 'classnames'

import { type LinkProps, Position, LinkTarget } from './types'

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
            {icon && iconPosition === Position.LEFT && <i className={icon} />}
            {label && <span>{label}</span>}
            {icon && iconPosition === Position.RIGHT && <i className={icon} />}
        </>
    )

    const classes = classNames(className, { disabled })

    const props = {
        className: classes,
        style,
        rel: target === LinkTarget.BLANK ? 'noopener noreferrer' : undefined,
        target,
    }

    if (disabled) { return <span {...props} aria-disabled>{content}</span> }

    return <a {...props} href={url} onClick={onClick}>{content}</a>
}
