import React from 'react'
import classNames from 'classnames'

import { ISelect } from './constants'

export function Select(props: ISelect) {
    const { title, onClick, style, className, active, icon, disabled, visible = true } = props

    if (!visible) {
        return null
    }

    return (
        <div
            onClick={onClick}
            style={style}
            className={classNames(className, { active, disabled })}
        >
            {icon && <i className={icon} />}
            {title && <span className="title">{title}</span>}
        </div>
    )
}
