import React from 'react'

import { type RowProps, Align, Justify } from './types'
import { Row } from './Row'

export function FlexRow({ children, wrap = true, align = Align.TOP, justify = Justify.START }: RowProps) {
    return <Row columns={null} wrap={wrap} align={align} justify={justify}>{children}</Row>
}
