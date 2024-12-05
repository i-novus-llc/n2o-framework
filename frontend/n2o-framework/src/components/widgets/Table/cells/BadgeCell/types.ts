import { RefObject } from 'react'

import { Shape, Position } from '../../../../snippets/Badge/enums'

export interface Props {
    id: string
    model: Record<string, string>
    fieldKey: string
    placement?: Position
    text: string
    format: string
    badgeFormat: string
    visible?: boolean
    className: string
    shape: Shape
    forwardedRef: RefObject<HTMLElement>
    color?: 'secondary' | 'primary' | 'danger' | 'success' | 'warning' | 'info'
    tooltipFieldId: string
    colorFieldId?: string
    imageFieldId?: string
    imagePosition?: Position
    imageShape?: Shape
}
