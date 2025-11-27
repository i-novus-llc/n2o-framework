import React, { CSSProperties } from 'react'
import classNames from 'classnames'

import { type RowProps, Align, Justify, ALIGN_CLASS_MAP, JUSTIFY_CLASS_MAP, COLUMNS } from './types'

import '../../styles/components/Row.scss'

export function Row({
    id,
    children,
    className,
    style,
    columns = 12,
    wrap = true,
    align = Align.TOP,
    justify = Justify.START,
}: RowProps) {
    const rowClasses = classNames(
        'layout-row',
        {
            'layout-row--wrap': wrap,
            'layout-row--nowrap': !wrap,
        },
        ALIGN_CLASS_MAP[align],
        JUSTIFY_CLASS_MAP[justify],
        className,
    )

    const styleProp: CSSProperties = { ...style, ...(columns != null && { [COLUMNS]: columns }) }

    return <div id={id} className={rowClasses} style={styleProp}>{children}</div>
}
