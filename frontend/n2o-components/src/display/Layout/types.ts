import { CSSProperties, ReactNode, HTMLAttributes } from 'react'

export type RowItemAlign  = 'top' | 'middle' | 'bottom' | 'stretch'

type ExcludeString<T> = T extends T
    ? (string extends T ? never : T)
    : never
type AlignItems = ExcludeString<CSSProperties['alignItems']>

export const AlignItemsMap: Record<RowItemAlign, AlignItems> = {
    top: 'start',
    middle: 'center',
    bottom: 'end',
    stretch: 'stretch',
}

/** Style param в DOM для активации колоночной системы */
export const COLUMNS = '--columns'

export interface RowProps {
    columns?: number | null
    id?: string
    children?: ReactNode
    wrap?: boolean
    align?: RowItemAlign
    justify?: CSSProperties['justifyContent']
    className?: string
    style?: CSSProperties
}

/** Style param в DOM для управления колонками */
export const COL_SPAN = '--col-span'
export const COL_OFFSET = '--col-offset'

export interface ColProps extends HTMLAttributes<HTMLDivElement> {
    children: ReactNode
    id?: string
    className?: string
    style?: CSSProperties
    size?: number
    offset?: number
}
