import { CSSProperties, Ref } from 'react'
import { Action } from 'redux'

import { Shape, TextPosition } from '../../../../snippets/Image/Image'
import { type Props as ActionWrapperProps } from '../../../../buttons/StandardButton/ActionWrapper'

interface Status {
    src: string
    fieldId: string
    place: string
}

export interface ImageStatusesType {
    statuses: Status[]
    className?: string
    model: Record<string, unknown>
    onClick?(): void
}

export interface ImageCellProps extends ActionWrapperProps {
    title: string
    fieldKey: string
    style?: CSSProperties
    className?: string
    model: Record<string, string>
    id: string
    shape: Shape
    visible: boolean
    disabled: boolean
    description?: string
    textPosition: TextPosition
    width: string
    height: number
    data: Array<Record<string, unknown>>
    forwardedRef: Ref<HTMLDivElement>
    callAction(model: Record<string, string>): void
    statuses: Status[]
    tooltipFieldId?: string
}
