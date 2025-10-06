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
    iconPosition = Position.LEFT,
    visible = true,
    enabled = true,
}: LinkProps) {
    if (!visible) { return null }

    const content = (
        <>
            {icon && iconPosition === Position.LEFT && <i className={icon} />}
            {label && <span>{label}</span>}
            {icon && iconPosition === Position.RIGHT && <i className={icon} />}
        </>
    )

    const classes = classNames(className, { disabled: !enabled })

    if (enabled && url) {
        return (
            <a
                href={url}
                rel={target === LinkTarget.BLANK ? 'noopener noreferrer' : undefined}
                target={target}
                className={classes}
                style={style}
            >
                {content}
            </a>
        )
    }

    return (
        <span
            className={classes}
            style={style}
            aria-disabled
        >
            {content}
        </span>
    )
}
