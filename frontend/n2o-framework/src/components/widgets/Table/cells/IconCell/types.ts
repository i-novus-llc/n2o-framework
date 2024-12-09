import { CSSProperties, LegacyRef } from 'react'

export enum ICON_POSITION {
    LEFT = 'left',
    RIGHT = 'right',
}

export interface Props {
    id: string
    model: Record<string, string>
    icon?: string
    forwardedRef: LegacyRef<HTMLDivElement>
    style?: CSSProperties
    className?: string
    iconPosition: ICON_POSITION
    visible: boolean
    tooltipFieldId?: string
}
