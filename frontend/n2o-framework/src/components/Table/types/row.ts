/* eslint-disable @typescript-eslint/no-explicit-any */
import { VFC, TdHTMLAttributes } from 'react'

export type Row = {
    click?: { enablingCondition?: string, [key: string]: unknown }
    component?: VFC<any>
    elementAttributes?: Partial<TdHTMLAttributes<HTMLTableRowElement>>
    hasSelect?: boolean
    security?: Record<string, any>
}
