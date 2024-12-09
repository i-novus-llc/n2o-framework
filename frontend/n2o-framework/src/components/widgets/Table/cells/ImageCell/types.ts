import { CSSProperties, Ref } from 'react'
import { Action } from 'redux'

import { Shape, TextPosition } from '../../../../snippets/Image/Image'

interface Status {
    src: string
    fieldId: string
    place: string
}

export interface ImageStatusesType {
    statuses: Status[]
    className?: string
    model: Record<string, unknown>
    onClick(): void
}

export interface ImageCellProps {
    title: string
    fieldKey: string
    style?: CSSProperties
    className?: string
    model: Record<string, string>
    id: string
    action: Action
    shape: Shape
    visible: boolean
    disabled: boolean
    description?: string
    textPosition: TextPosition
    width: string
    height: number
    data: Array<Record<string, unknown>>
    pathMapping: Record<string, string>
    queryMapping: Record<string, string>
    target: string
    url: string
    forwardedRef: Ref<HTMLDivElement>
    callAction(model: Record<string, string>): void
    statuses: Status[]
    tooltipFieldId?: string
}
