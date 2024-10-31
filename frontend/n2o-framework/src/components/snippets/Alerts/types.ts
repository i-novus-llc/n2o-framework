import { CSSProperties, MouseEventHandler } from 'react'

export interface CommonAlertProps {
    className?: string
    onClick?: MouseEventHandler<HTMLElement>
    closeButton?: boolean
    stacktrace?: string | null
    stacktraceVisible?: boolean
    style?: CSSProperties | null
    title?: string | number | null
    text?: string | number | null
    t(key: string): string
    timestamp?: string
    extended?: boolean
}

export type SegmentProps = Omit<CommonAlertProps, 't'>
