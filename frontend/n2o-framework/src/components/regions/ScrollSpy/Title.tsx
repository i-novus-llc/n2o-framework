import React from 'react'

import { type TitleProps } from './types'

export function Title({ id, title, className, onClick, visible = true }: TitleProps) {
    if (!title || !visible) { return null }

    return <div onClick={onClick} id={id} className={className}>{title}</div>
}
