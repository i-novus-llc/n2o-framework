import React from 'react'

import { type RowProps, Align, Justify } from './types'
import { Row } from './Row'

export function FlexRow({
    children,
    className,
    style,
    wrap = true,
    align =
    Align.TOP,
    justify = Justify.START,
}: RowProps) {
    return (
        <Row
            columns={null}
            className={className}
            style={style}
            wrap={wrap}
            align={align}
            justify={justify}
        >
            {children}
        </Row>
    )
}
