/* eslint-disable @typescript-eslint/no-explicit-any */
import { FC, VFC } from 'react'

export type Row = {
    click?: { enablingCondition?: string, [key: string]: unknown }
    component?: VFC<any>
    elementAttributes?: Record<string, any>
    hasSelect?: boolean
    security?: Record<string, any>
    overlay?: {
        className?: string
        toolbar: Array<{
            id: string
            buttons: Array<{
                component: FC<any>
                id: string
                label: string
                [x: string]: any
            }>
        }>
        componentAttributes: Record<string, any>
    }
}
