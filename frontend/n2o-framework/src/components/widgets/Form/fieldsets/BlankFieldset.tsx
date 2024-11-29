import React from 'react'

import { withFieldsetHeader } from './withFieldsetHeader'
import { DefaultFieldset } from './DefaultFieldset'
import { type FieldsetProps } from './types'

export type Props = Pick<FieldsetProps, 'rows' | 'render' | 'disabled'>

function BlankFieldsetBody({ render, rows = [], disabled = false }: Props) {
    return <DefaultFieldset disabled={disabled} className="blank-fieldset">{render(rows)}</DefaultFieldset>
}

export const BlankFieldset = withFieldsetHeader(BlankFieldsetBody)
export default BlankFieldset
