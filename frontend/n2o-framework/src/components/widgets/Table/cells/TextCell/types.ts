import { LegacyRef, CSSProperties } from 'react'

export enum ICON_POSITIONS {
    LEFT = 'left',
    RIGHT = 'right',
}

export interface SubTextType {
    format: string
    subText: string[] | string
}

export interface TextCellProps {
    className?: string
    fieldKey: string
    format?: string
    forwardedRef: LegacyRef<HTMLDivElement>
    icon: string
    iconPosition: ICON_POSITIONS
    id: string
    isTextWrap?: boolean
    model: Record<string, string>
    style?: CSSProperties
    subTextFieldKey: string
    subTextFormat: string
    tooltipFieldId: string
    visible: boolean
}
