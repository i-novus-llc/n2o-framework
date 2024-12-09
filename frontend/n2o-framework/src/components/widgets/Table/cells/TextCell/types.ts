import { LegacyRef } from 'react'

export enum ICON_POSITIONS {
    LEFT = 'left',
    RIGHT = 'right',
}

export interface SubTextType {
    subText: string[] | string
    format: string
}

export interface TextCellProps {
    fieldKey: string
    icon: string
    iconPosition: ICON_POSITIONS
    id: string
    model: Record<string, string>
    preLine: boolean
    subTextFieldKey: string
    subTextFormat: string
    visible: boolean
    forwardedRef: LegacyRef<HTMLDivElement>
    tooltipFieldId: string
}
