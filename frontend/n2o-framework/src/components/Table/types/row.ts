/* eslint-disable @typescript-eslint/no-explicit-any */
import { VFC, TdHTMLAttributes } from 'react'

export type Row = {
    click?: Record<string, any>
    component?: VFC<any>
    elementAttributes?: Partial<TdHTMLAttributes<HTMLTableRowElement>>
    hasSelect?: boolean
    security?: Record<string, any>
}
