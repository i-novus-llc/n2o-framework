import React from 'react'
import classNames from 'classnames'

import { type SelectProps } from './constants'

export function Select({ title, onClick, style, className, active, icon, disabled, visible = true }: SelectProps) {
    if (!visible) { return null }

    return (
        <button
            type="button"
            onClick={onClick}
            style={style}
            className={classNames(className, { active, disabled })}
            disabled={disabled}
        >
            {icon && <i className={icon} />}
            {title && <span className="title">{title}</span>}
        </button>
    )
}
