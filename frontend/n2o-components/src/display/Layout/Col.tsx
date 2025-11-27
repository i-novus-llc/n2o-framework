import React, { CSSProperties } from 'react'
import classNames from 'classnames'

import { type ColProps, COL_SPAN, COL_OFFSET } from './types'

export function Col({
    children,
    id,
    className,
    style,
    size = 1,
    offset = 0,
}: ColProps) {
    const colStyles = {
        ...style,
        [COL_SPAN]: size,
        [COL_OFFSET]: offset,
    } as CSSProperties

    return (
        <div
            id={id}
            className={classNames('layout-col', className)}
            style={colStyles}
        >
            {children}
        </div>
    )
}
